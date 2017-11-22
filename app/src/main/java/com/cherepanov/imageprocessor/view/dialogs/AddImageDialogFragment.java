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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cherepanov.imageprocessor.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddImageDialogFragment extends DialogFragment {

    public interface AddImageDialogListener {
        void onMakePhoto();

        void onTakeFromStorage();

        void loadFromURL(String url);
    }

    @Bind(R.id.make_photo_btn)
    Button mMakePhotoBtn;

    @Bind(R.id.take_from_storage_btn)
    Button mTakeStorageBtn;
    @Bind(R.id.load_from_internet_btn)
    Button mURLBtn;
    @Bind(R.id.input_edit_text)
    EditText mInputUrlET;
    @Bind(R.id.input_layout)
    LinearLayout mInputLayout;
    @Bind(R.id.download_btn)
    Button mDownloadBtn;

    AddImageDialogListener mDialogListener;

    public void setDialogListener(AddImageDialogListener listener) {
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
                if (mDialogListener != null) {
                    mDialogListener.onMakePhoto();
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
                AddImageDialogFragment.this.getDialog().dismiss();
            }
        });

        mTakeStorageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogListener != null) {
                    mDialogListener.onTakeFromStorage();
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
                AddImageDialogFragment.this.getDialog().dismiss();
            }
        });
        mURLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogListener != null) {
                    mInputLayout.setVisibility(View.VISIBLE);
                    mURLBtn.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogListener != null) {
                    if (mInputUrlET.getText().toString().isEmpty()){
                        Toast.makeText(getContext(), R.string.please_enter, Toast.LENGTH_SHORT).show();
                    } else {
                        mDialogListener.loadFromURL(mInputUrlET.getText().toString());
                    }
                } else {
                    Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
                AddImageDialogFragment.this.getDialog().dismiss();
            }
        });
    }
}