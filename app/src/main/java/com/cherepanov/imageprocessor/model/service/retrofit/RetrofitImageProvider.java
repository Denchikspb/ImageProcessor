package com.cherepanov.imageprocessor.model.service.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImageProvider {

    public static ImageService getApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://your.api.url/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ImageService.class);
    }
}
