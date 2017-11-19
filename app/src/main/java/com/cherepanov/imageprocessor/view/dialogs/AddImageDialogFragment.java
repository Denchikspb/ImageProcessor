package com.cherepanov.imageprocessor.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.cherepanov.imageprocessor.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddImageDialogFragment extends DialogFragment {

    public interface AddImageDialogListener {
       void onMakePhoto();

       void onTakeFromStorage();
    }

    @Bind(R.id.make_photo_btn)
    Button mMakePhotoBtn;

    @Bind(R.id.take_from_storage_btn)
    Button mTakeStorageBtn;

    AddImageDialogListener mDialogListener;

    public void setDialogListener(AddImageDialogListener listener){
        mDialogListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_image_dialog_layout, null);
        ButterKnife.bind(this, view);
        builder.setTitle(R.string.add_image)
                .setView(view)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AddImageDialogFragment.this.getDialog().dismiss();
                    }
                });

        setupListeners();
        return builder.create();
    }

    private void setupListeners() {
        mMakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogListener.onMakePhoto();
                AddImageDialogFragment.this.getDialog().dismiss();
            }
        });

        mTakeStorageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialogListener.onTakeFromStorage();
                AddImageDialogFragment.this.getDialog().dismiss();
            }
        });
    }
}