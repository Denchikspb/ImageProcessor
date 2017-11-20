package com.cherepanov.imageprocessor.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.cherepanov.imageprocessor.R;

public class ClickItemAdapterDialog extends DialogFragment {

    private static final String ITEM_NUM = "ITEM_NUM";

    public interface ItemAdapterDialogListener {
        void deleteImage(int numberItem);

        void useImageAsSrc(int numberItem);
    }

    ItemAdapterDialogListener mListener;
    private int mNumberItem;

    public void setNumberItem(int numberItem) {
        this.mNumberItem = numberItem;
    }

    public void setListener(ItemAdapterDialogListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mNumberItem = savedInstanceState.getInt(ITEM_NUM);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.select_next_step)
                .setMessage(R.string.item_dialog_message)
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.deleteImage(mNumberItem);
                        } else {
                            Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClickItemAdapterDialog.this.getDialog().dismiss();

                    }
                })
                .setNeutralButton(R.string.source, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mListener != null) {
                            mListener.useImageAsSrc(mNumberItem);
                        } else {
                            Toast.makeText(getContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(ITEM_NUM, mNumberItem);

        super.onSaveInstanceState(outState);
    }
}
