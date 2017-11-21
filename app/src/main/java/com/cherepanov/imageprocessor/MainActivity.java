package com.cherepanov.imageprocessor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cherepanov.imageprocessor.view.listImage.ListImageFragment;
import com.cherepanov.imageprocessor.view.processor.ImageProcessFragment;

public class MainActivity extends AppCompatActivity implements
        ImageProcessFragment.OnFragmentInteractionListener,
        ListImageFragment.OnListInteractionListener {

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!hasPermissions()){
            requestPermissionWithRationale();
        }
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

    public boolean hasPermissions(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPerms(){
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions,PERMISSION_REQUEST_CODE);
        }
    }

    public void requestMultiplePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showExtDirFilesCount();
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                showUnreadSmsCount();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            final String message = "Storage permission is needed to show images";
            Snackbar.make(MainActivity.this.findViewById(R.id.main_activity), message, Snackbar.LENGTH_LONG)
                    .setAction("GRANT", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPerms();
                        }
                    })
                    .show();
        } else {
            requestPerms();
        }
    }
}