package com.cherepanov.imageprocessor.presenter.processor;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

/**
 * Created by Денис on 19.11.2017.
 */

public interface IImagePresenter {

    void rotateImage(ImageView srcImage);
    void invertColorsImg(ImageView srcImage);
    void mirrorImg(ImageView srcImage);
    void addImageDialog(FragmentManager manager);
    void saveSrcImage(Drawable drawable);
    void showData();

    Bitmap getBitmapByURI(Uri uriImage);
}
