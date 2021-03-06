package com.cherepanov.imageprocessor.view.processor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cherepanov.imageprocessor.R;
import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.presenter.processor.ImageProcessPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class ImageProcessFragment
        extends MvpFragment<IImageProcessView, ImageProcessPresenter>
        implements IImageProcessView {

    @Bind(R.id.rotate_btn)
    Button mRotateBtn;
    @Bind(R.id.invert_btn)
    Button mInvertBtn;
    @Bind(R.id.mirror_btn)
    Button mMirrorBtn;
    @Bind(R.id.add_image_btn)
    Button mAddImageBtn;

    @Bind(R.id.image_view)
    ImageView mImage;
    @Bind(R.id.progress_download)
    ProgressBar mProgressBar;

    private final int CAMERA_RESULT = 0;
    private final int RESULT_LOAD_IMAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 123;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(ImageFile imageFile);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_process, container, false);
        ButterKnife.bind(this, view);
        setupListeners();
        return view;
    }

    @NonNull
    @Override
    public ImageProcessPresenter createPresenter() {
        return new ImageProcessPresenter(getContext());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().showData();
    }

    @Override
    public void onStop() {
        super.onStop();
        getPresenter().saveSrcImage(mImage.getDrawable());
        getPresenter().onStop();
    }

    @Override
    public void showNewImage(ImageFile imageFile) {
        mListener.onFragmentInteraction(imageFile);
    }

    @Override
    public void showMainImage(Bitmap bitmap) {
        mImage.setVisibility(View.VISIBLE);
        mImage.setImageBitmap(bitmap);
        mAddImageBtn.setVisibility(View.GONE);
    }

    @Override
    public void makePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_RESULT);
    }

    @Override
    public void getFromGallery() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        mAddImageBtn.setVisibility(View.GONE);
        mImage.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading(boolean success) {
        mProgressBar.setVisibility(View.GONE);
        mImage.setVisibility(success ? View.VISIBLE : View.GONE);
        mAddImageBtn.setVisibility(success ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Object obj = bundle.get("data");
                if (obj instanceof Bitmap) {
                    Bitmap thumbnailBitmap = (Bitmap) obj;
                    mImage.setImageBitmap(thumbnailBitmap);
                    mAddImageBtn.setVisibility(View.GONE);
                }
            }
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = getPresenter().getBitmapByURI(data.getData());
            mImage.setImageBitmap(null);
            mImage.setImageBitmap(bitmap);
            mAddImageBtn.setVisibility(View.GONE);
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            getFromGallery();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addNewSrcImage(Bitmap bitmap) {
        mAddImageBtn.setVisibility(View.GONE);
        mImage.setImageBitmap(bitmap);
    }

    private void setupListeners() {

        mRotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().rotateImage(mImage);
            }
        });

        mInvertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().invertColorsImg(mImage);
            }
        });

        mMirrorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().mirrorImg(mImage);
            }
        });

        mAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().addImageDialog(getFragmentManager());
            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPresenter().addImageDialog(getFragmentManager());
            }
        });
    }
}