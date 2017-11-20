package com.cherepanov.imageprocessor.view.processor;

import android.graphics.Bitmap;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IImageProcessView extends MvpView {

    void showNewImage(Bitmap bitmap);

    void showMainImage(Bitmap bitmap);

    void makePhoto();

    void getFromGallery();
}
