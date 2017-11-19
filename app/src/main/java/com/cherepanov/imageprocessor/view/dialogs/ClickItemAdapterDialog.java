package com.cherepanov.imageprocessor.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.cherepanov.imageprocessor.R;

public class ClickItemAdapterDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle(R.string.select_next_step)
                .setMessage(R.string.item_dialog_message)
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClickItemAdapterDialog.this.getDialog().dismiss();
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
                        ClickItemAdapterDialog.this.getDialog().dismiss();

                    }
                });

        return builder.create();
    }
}
