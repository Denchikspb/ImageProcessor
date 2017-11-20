package com.cherepanov.imageprocessor.presenter.listImage;

import android.content.Context;
import android.graphics.Bitmap;

import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.view.adapter.ResultImageAdapter;
import com.cherepanov.imageprocessor.view.listImage.IListImageView;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.List;

public class ListImagePresenter
        extends MvpBasePresenter<IListImageView>
        implements IListImagePresenter {

    Context mContext;
    List<ImageFile> mCurrentImageFiles;
    ResultImageAdapter mAdapter;

    public ListImagePresenter(Context context) {
        mContext = context;
        mCurrentImageFiles = new ArrayList<>();
    }

    @Override
    public void addNewImage(Bitmap bitmap) {
        ImageFile file = new ImageFile();
        file.setBitmap(bitmap);
        mCurrentImageFiles.add(file);
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
}
