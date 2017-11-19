package com.cherepanov.imageprocessor.view.listImage;

import android.graphics.Bitmap;

import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface IListImageView extends MvpView {

    void showImageList(List<ImageFile> list);
}
