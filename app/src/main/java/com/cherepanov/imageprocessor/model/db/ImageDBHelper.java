package com.cherepanov.imageprocessor.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cherepanov.imageprocessor.model.db.tables.ImageFileTable;


public class ImageDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "images.db";

    private static ImageDBHelper sDBHelper;

    public static ImageDBHelper getInstance(Context context) {
        if (sDBHelper == null) {
            sDBHelper = new ImageDBHelper(context);
        }
        return sDBHelper;
    }

    private ImageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ImageFileTable.CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(ImageFileTable.DROP_IMAGE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
