package com.cherepanov.imageprocessor;

import android.Manifest;
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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

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

        if (hasPermissions()) {
            makeFolder();
        } else {
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

    public boolean hasPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPerms() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            makeFolder();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        boolean allowed = true;
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int res : grantResults) {
                allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
            }
        }
        if (allowed) {
            makeFolder();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Storage Permissions denied.", Toast.LENGTH_SHORT).show();

                } else {
                    showNoStoragePermissionSnackbar();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(MainActivity.this.findViewById(R.id.main_activity), "Storage permission isn't granted", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                "Open Permissions and grant the Storage permission",
                                Toast.LENGTH_SHORT)
                                .show();
                    }
                }).show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
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