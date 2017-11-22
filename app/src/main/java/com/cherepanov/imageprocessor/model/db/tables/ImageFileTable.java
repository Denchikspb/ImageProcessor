package com.cherepanov.imageprocessor.model.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cherepanov.imageprocessor.model.db.ImageDBHelper;
import com.cherepanov.imageprocessor.model.entity.ImageFile;

import java.util.ArrayList;
import java.util.List;

public class ImageFileTable {

    private static final String LOG_TAG = ImageFileTable.class.getSimpleName();

    private static final String TABLE_NAME = "images";

    private static final String ID = "id";
    private static final String PATH_IMAGE = "path";

    public static final String CREATE_IMAGE_TABLE = "create table " + TABLE_NAME + "(" +
            "_id integer primary key autoincrement, " +
            ID + "," +
            PATH_IMAGE + ")";

    public static final String DROP_IMAGE_TABLE = "drop table if exists" + TABLE_NAME;

    public static void addImageFile(ImageDBHelper helper, ImageFile imageFile) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID, imageFile.getId().toString());
        values.put(PATH_IMAGE, imageFile.getPathFile());

        try {
            db.insert(TABLE_NAME, null, values);
        } catch (SQLException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

        db.close();
    }

    public static List<ImageFile> getAll(ImageDBHelper helper) {
        List<ImageFile> imageFiles = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            imageFiles.add(getImage(cursor));
                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();
            }
        } catch (SQLException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        return imageFiles;
    }

    public static void deleteImage(ImageDBHelper helper, ImageFile imageFile) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int delCount = db.delete(TABLE_NAME, ID + " = ?", new String[]{imageFile.getId().toString()});
        Log.d(LOG_TAG, " count delete = " + delCount);
    }

    private static ImageFile getImage(Cursor cursor) {
        ImageFile file = new ImageFile();
        file.setId(java.util.UUID.fromString(cursor.getString(cursor.getColumnIndex(ID))));
        file.setPathFile(cursor.getString(cursor.getColumnIndex(PATH_IMAGE)));

        return file;
    }

}
