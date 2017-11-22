package com.cherepanov.imageprocessor.view.processor;

import android.graphics.Bitmap;

import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IImageProcessView extends MvpView {

    void showNewImage(ImageFile imageFile);

    void showMainImage(Bitmap bitmap);

    void makePhoto();

    void getFromGallery();

    void showMessage(String text);

    void showLoading();

    void hideLoading(boolean success);
}
