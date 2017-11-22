package com.cherepanov.imageprocessor.presenter.processor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;

import com.cherepanov.imageprocessor.R;
import com.cherepanov.imageprocessor.model.db.ImageDBHelper;
import com.cherepanov.imageprocessor.model.db.tables.ImageFileTable;
import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.model.service.IService;
import com.cherepanov.imageprocessor.model.service.Service;
import com.cherepanov.imageprocessor.model.storage.ILocalStorage;
import com.cherepanov.imageprocessor.model.storage.LocalStorageImpl;
import com.cherepanov.imageprocessor.utils.NetworkUtils;
import com.cherepanov.imageprocessor.view.dialogs.AddImageDialogFragment;
import com.cherepanov.imageprocessor.view.processor.IImageProcessView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class ImageProcessPresenter
        extends MvpBasePresenter<IImageProcessView>
        implements IImagePresenter, AddImageDialogFragment.AddImageDialogListener {

    private Context mContext;
    private ILocalStorage mStorage;
    private Drawable mSrcDrawable;
    private ImageDBHelper mDBHelper;
    private IService mService = new Service();
    private Subscription mSubscription = Subscriptions.empty();

    public ImageProcessPresenter(Context context) {
        mContext = context;
        mStorage = LocalStorageImpl.getInstance(mContext);
        mDBHelper = ImageDBHelper.getInstance(mContext);
    }

    @Override
    public void rotateImage(ImageView srcImage) {
        if (!isNotNullImage(srcImage.getDrawable())) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) srcImage.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (isViewAttached()) {
            ImageFile imageFile = saveImage(newBitmap);
            getView().showNewImage(imageFile);
        }
    }

    @Override
    public void invertColorsImg(ImageView srcImage) {
        if (!isNotNullImage(srcImage.getDrawable())) {
            return;
        }
        Bitmap src = ((BitmapDrawable) srcImage.getDrawable()).getBitmap();

        Bitmap dest = Bitmap.createBitmap(
                src.getWidth(), src.getHeight(), src.getConfig());
        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                int pixelColor = src.getPixel(x, y);
                int pixelAlpha = Color.alpha(pixelColor);
                int pixelRed = Color.red(pixelColor);
                int pixelGreen = Color.green(pixelColor);
                int pixelBlue = Color.blue(pixelColor);

                int pixelBW = (pixelRed + pixelGreen + pixelBlue) / 3;
                int newPixel = Color.argb(
                        pixelAlpha, pixelBW, pixelBW, pixelBW);

                dest.setPixel(x, y, newPixel);
            }
        }

        if (isViewAttached()) {
            ImageFile imageFile = saveImage(dest);
            getView().showNewImage(imageFile);
        }
    }

    @Override
    public void mirrorImg(ImageView srcImage) {
        if (!isNotNullImage(srcImage.getDrawable())) {
            return;
        }
        Bitmap bitmap = ((BitmapDrawable) srcImage.getDrawable()).getBitmap();
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (isViewAttached()) {
            ImageFile imageFile = saveImage(newBitmap);
            getView().showNewImage(imageFile);
        }
    }

    @Override
    public void addImageDialog(FragmentManager manager) {
        AddImageDialogFragment dialog = new AddImageDialogFragment();
        dialog.setDialogListener(this);
        dialog.show(manager, "AddDialogFragment");
    }

    @Override
    public void onMakePhoto() {
        if (isViewAttached()) {
            getView().makePhoto();
        }
    }

    @Override
    public void onTakeFromStorage() {
        if (isViewAttached()) {
            getView().getFromGallery();
        }
    }

    @Override
    public void loadFromURL(String url) {
        if (NetworkUtils.isNetAvailable(mContext)) {
            startLoadFromURL(url);
        } else {
            if (isViewAttached()) {
                getView().showMessage(mContext.getResources().getString(R.string.no_internet));
            }
        }
    }

    @Override
    public Bitmap getBitmapByURI(Uri uriImage) {
        return mStorage.getBitmapByURI(uriImage);
    }

    @Override
    public void saveSrcImage(Drawable drawable) {
        mSrcDrawable = drawable;
    }

    @Override
    public void showData() {
        if (isViewAttached() && mSrcDrawable != null) {
            getView().showMainImage(((BitmapDrawable) mSrcDrawable).getBitmap());
        }
    }

    private void startLoadFromURL(String url) {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        if (isViewAttached()){
            getView().showMessage("start load");
            mSubscription = mService.loadImage(url)
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                            getView().showMessage("load completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            getView().showMessage("load failed");
                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {
                            Bitmap bitmap = downloadImage(responseBody);
                            if (isViewAttached()){
                                getView().showMainImage(bitmap);
                            }
                        }
                    });
        }
    }

    public void onStop() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private Bitmap downloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            ImageFile imageFile = new ImageFile();
            File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ImageProcessor");
            File mypath = new File(directory, imageFile.getId() + ".jpg");

            try {
                in = body.byteStream();
                out = new FileOutputStream(mypath);
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return null;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            return mStorage.loadBitmapImageFile(imageFile);

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return null;
        }
    }

    private boolean isNotNullImage(Drawable image) {
        if (image == null) {
            if (isViewAttached()) {
                getView().showMessage(mContext.getResources().getString(R.string.no_image));
                return false;
            }
        }
        return true;
    }

    private ImageFile saveImage(Bitmap newBitmap){
        ImageFile imageFile = new ImageFile();
        imageFile.setBitmap(newBitmap);
        String path = mStorage.saveToInternalStorage(imageFile);
        imageFile.setPathFile(path);
        if (imageFile.getPathFile() != null && !imageFile.getPathFile().isEmpty()){
            ImageFileTable.addImageFile(mDBHelper, imageFile);
        }

        return imageFile;
    }
}