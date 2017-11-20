package com.cherepanov.imageprocessor.view.adapter;

import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cherepanov.imageprocessor.R;
import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.view.dialogs.ClickItemAdapterDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResultImageAdapter
        extends RecyclerView.Adapter<ResultImageAdapter.ViewHolder>
        implements ClickItemAdapterDialog.ItemAdapterDialogListener {

    public interface ImageAdapterListener{
        void deleteImage(int number);

        void useImageAsSrc(int number);
    }

    ImageAdapterListener mListener;

    public void setListener(ImageAdapterListener listener) {
        mListener = listener;
    }

    List<ImageFile> mImageFiles = new ArrayList<>();

    FragmentManager mFragmentManager;

    public ResultImageAdapter(FragmentManager manager) {
        mFragmentManager = manager;
    }

    public void setCurrentListImage(List<ImageFile> list) {
        mImageFiles = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mImageView.setImageBitmap(mImageFiles.get(position).getBitmap());
        holder.mLayout.setBackgroundColor(position % 2 == 0 ? Color.WHITE : Color.BLACK);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickItemAdapterDialog dialog = new ClickItemAdapterDialog();
                dialog.setListener(ResultImageAdapter.this);
                dialog.setNumberItem(position);
                dialog.show(mFragmentManager, "Dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageFiles == null ? 0 : mImageFiles.size();
    }

    @Override
    public void deleteImage(int number) {
        mListener.deleteImage(number);
    }

    @Override
    public void useImageAsSrc(int number) {
        mListener.useImageAsSrc(number);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_image)
        ImageView mImageView;
        @Bind(R.id.item_layout)
        RelativeLayout mLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
