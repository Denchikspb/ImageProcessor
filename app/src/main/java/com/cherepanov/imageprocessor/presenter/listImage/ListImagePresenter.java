package com.cherepanov.imageprocessor.presenter.listImage;

import android.content.Context;
import android.graphics.Bitmap;

import com.cherepanov.imageprocessor.model.db.ImageDBHelper;
import com.cherepanov.imageprocessor.model.db.tables.ImageFileTable;
import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.model.storage.ILocalStorage;
import com.cherepanov.imageprocessor.model.storage.LocalStorageImpl;
import com.cherepanov.imageprocessor.view.adapter.ResultImageAdapter;
import com.cherepanov.imageprocessor.view.listImage.IListImageView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

public class ListImagePresenter
        extends MvpBasePresenter<IListImageView>
        implements IListImagePresenter {

    private Context mContext;
    private List<ImageFile> mCurrentImageFiles;
    private ImageDBHelper mDBHelper;
    private ILocalStorage mStorage;

    public ListImagePresenter(Context context) {
        mContext = context;
        mCurrentImageFiles = new ArrayList<>();
        mDBHelper = ImageDBHelper.getInstance(mContext);
        mStorage = LocalStorageImpl.getInstance(mContext);
        loadCache();
    }

    @Override
    public void addNewImage(ImageFile imageFile) {
        mCurrentImageFiles.add(imageFile);
        if (isViewAttached()) {
            getView().showImageList(mCurrentImageFiles);
        }
    }

    @Override
    public void showData() {
        if (isViewAttached()) {
            getView().showImageList(mCurrentImageFiles);
        }
    }

    @Override
    public void deleteItem(int number) {
        mCurrentImageFiles.remove(number);
        showData();
    }

    @Override
    public Bitmap getImageFromList(int number) {
        if (mCurrentImageFiles != null) {
            return mCurrentImageFiles.get(number).getBitmap();
        }
        return null;
    }


    private void loadCache() {
        List<ImageFile> listImages = ImageFileTable.getAll(mDBHelper);
        if (listImages != null && !listImages.isEmpty()){
            for (int i = 0; i < listImages.size(); i++) {
                ImageFile imageFile = listImages.get(i);
                Bitmap bitmap = mStorage.loadBitmapImageFile(imageFile);
                if (bitmap != null){
                    imageFile.setBitmap(bitmap);
                    mCurrentImageFiles.add(imageFile);
                }
            }
        }
        if(isViewAttached()){
            getView().showImageList(mCurrentImageFiles);
        }
    }
}
