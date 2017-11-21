package com.cherepanov.imageprocessor.presenter.listImage;

import android.graphics.Bitmap;

import com.cherepanov.imageprocessor.model.entity.ImageFile;

public interface IListImagePresenter {

    void addNewImage(ImageFile imageFile);
    void showData();
    void deleteItem(int number);
    Bitmap getImageFromList(int number);
}
