package com.cherepanov.imageprocessor.model.service;

import com.cherepanov.imageprocessor.model.service.retrofit.ImageService;
import com.cherepanov.imageprocessor.model.service.retrofit.RetrofitImageProvider;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Service implements IService{

    ImageService mService = RetrofitImageProvider.getApiService();

    @Override
    public Observable<ResponseBody> loadImage(String url){
        return mService.downloadImage(url)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
