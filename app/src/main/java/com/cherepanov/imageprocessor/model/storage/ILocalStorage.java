package com.cherepanov.imageprocessor.model.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.cherepanov.imageprocessor.model.entity.ImageFile;

public interface ILocalStorage {

    Bitmap loadBitmapImageFile(ImageFile imageFile);

    Bitmap getBitmapByURI(Uri uri);

    String saveToInternalStorage(ImageFile imageFile);
}
