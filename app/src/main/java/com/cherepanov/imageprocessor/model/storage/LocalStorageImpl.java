package com.cherepanov.imageprocessor.model.storage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.cherepanov.imageprocessor.utils.Constants;
import com.cherepanov.imageprocessor.utils.PictureUtils;

public class LocalStorageImpl implements ILocalStorage {

    private Context mContext;

    public LocalStorageImpl(Context context) {
        mContext = context;
    }

    @Override
    public Bitmap getBitmapByURI(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = mContext.getContentResolver().query(uri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return PictureUtils.getResizedBitmap(BitmapFactory.decodeFile(picturePath), Constants.MAX_SIZE_IMAGE);
    }
}
