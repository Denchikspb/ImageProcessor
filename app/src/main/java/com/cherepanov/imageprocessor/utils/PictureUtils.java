package com.cherepanov.imageprocessor.utils;

import android.graphics.Bitmap;

public class PictureUtils {

    /**
     *  Set Max size for image in list
     *
     * @param image - item image
     * @param maxSize - max size
     * @return - our image
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        if (image == null){
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
