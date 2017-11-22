package com.cherepanov.imageprocessor.model.entity;

import android.graphics.Bitmap;

import java.util.UUID;

public class ImageFile {

    private UUID mId;
    private String mPathFile;
    private Bitmap mBitmap;

    public ImageFile(){
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getPathFile() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public void setPathFile(String pathFile) {
        mPathFile = pathFile;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
}
