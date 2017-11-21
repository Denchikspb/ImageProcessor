package com.cherepanov.imageprocessor.model.storage;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.utils.Constants;
import com.cherepanov.imageprocessor.utils.PictureUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    @Override
    public String saveToInternalStorage(ImageFile imageFile, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ImageProcessor");

        File mypath = new File(directory, imageFile.getId() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            imageFile.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }


}
