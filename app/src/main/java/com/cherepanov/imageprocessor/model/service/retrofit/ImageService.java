package com.cherepanov.imageprocessor.model.service.retrofit;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface ImageService {

    @GET
    Observable<ResponseBody> downloadImage(@Url String url);
}
