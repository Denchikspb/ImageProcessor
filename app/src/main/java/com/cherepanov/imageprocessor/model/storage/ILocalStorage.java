package com.cherepanov.imageprocessor.model.storage;

import android.graphics.Bitmap;
import android.net.Uri;

public interface ILocalStorage {

    Bitmap getBitmapByURI(Uri uri);
}
