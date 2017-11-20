package com.cherepanov.imageprocessor.presenter.listImage;

import android.graphics.Bitmap;

public interface IListImagePresenter {

    void addNewImage(Bitmap bitmap);
    void showData();
    void deleteItem(int number);
    Bitmap getImageFromList(int number);
}
