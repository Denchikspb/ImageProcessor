package com.cherepanov.imageprocessor;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.view.listImage.ListImageFragment;
import com.cherepanov.imageprocessor.view.processor.ImageProcessFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity implements
        ImageProcessFragment.OnFragmentInteractionListener,
        ListImageFragment.OnListInteractionListener {

    private static final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isPermissionGranted()) {
            makeFolder();
        } else {
            requestPermission();
        }
    }

    @Override
    public void onFragmentInteraction(ImageFile imageFile) {
        ListImageFragment fragment = (ListImageFragment) getSupportFragmentManager()
                .findFragmentById(R.id.list_image_fragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.addNewImage(imageFile);
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

    private boolean isPermissionGranted() {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeFolder();
            } else {
                Toast.makeText(MainActivity.this, "No permits received", Toast.LENGTH_LONG).show();
                showPermissionDialog(MainActivity.this);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void showPermissionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String title = getResources().getString(R.string.app_name);
        builder.setTitle(title);
        builder.setMessage(title + "  requires permission to access the internal memory");

        String positiveText = "Settings";
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openAppSettings();
            }
        });

        String negativeText = "Exit";
        builder.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void openAppSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            requestApplicationConfig();
        }
    }

    private void requestApplicationConfig() {
        if (isPermissionGranted()) {
            Toast.makeText(MainActivity.this, "Permissions obtained", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "No permits received", Toast.LENGTH_LONG).show();
            requestPermission();
        }
    }

    private void makeFolder() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "ImageProcessor");

        if (!file.exists()) {
            Boolean ff = file.mkdir();
            if (ff) {
                Toast.makeText(MainActivity.this, "Folder created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to create folder", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(MainActivity.this, "Folder already exist", Toast.LENGTH_SHORT).show();
        }
    }
}