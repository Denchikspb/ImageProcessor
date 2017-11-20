package com.cherepanov.imageprocessor;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cherepanov.imageprocessor.view.listImage.ListImageFragment;
import com.cherepanov.imageprocessor.view.processor.ImageProcessFragment;

public class MainActivity extends AppCompatActivity implements
        ImageProcessFragment.OnFragmentInteractionListener,
        ListImageFragment.OnListInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onFragmentInteraction(Bitmap bitmap) {
        ListImageFragment fragment = (ListImageFragment) getSupportFragmentManager()
                .findFragmentById(R.id.list_image_fragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.addNewImage(bitmap);
        }
    }

    @Override
    public void passImageToSrc(Bitmap bitmap) {
        ImageProcessFragment fragment = (ImageProcessFragment) getSupportFragmentManager()
                .findFragmentById(R.id.image_fragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.addNewSrcImage(bitmap);
        }
    }
}