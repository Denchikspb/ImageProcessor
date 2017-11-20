package com.cherepanov.imageprocessor.view.listImage;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherepanov.imageprocessor.R;
import com.cherepanov.imageprocessor.model.entity.ImageFile;
import com.cherepanov.imageprocessor.presenter.listImage.ListImagePresenter;
import com.cherepanov.imageprocessor.view.adapter.ResultImageAdapter;
import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ListImageFragment
        extends MvpFragment<IListImageView, ListImagePresenter>
        implements IListImageView, ResultImageAdapter.ImageAdapterListener {

    public interface OnListInteractionListener {
        void passImageToSrc(Bitmap bitmap);
    }

    OnListInteractionListener mListener;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ResultImageAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_image, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ResultImageAdapter(getFragmentManager());
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnListInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().showData();
    }

    @NonNull
    @Override
    public ListImagePresenter createPresenter() {
        return new ListImagePresenter(getContext());
    }

    public void addNewImage(Bitmap bitmap) {
        getPresenter().addNewImage(bitmap);
    }

    @Override
    public void showImageList(List<ImageFile> list) {
        mAdapter.setCurrentListImage(list);
    }

    @Override
    public void deleteImage(int numberItem) {
        getPresenter().deleteItem(numberItem);
    }

    @Override
    public void useImageAsSrc(int numberItem) {
        mListener.passImageToSrc(getPresenter().getImageFromList(numberItem));
    }
}