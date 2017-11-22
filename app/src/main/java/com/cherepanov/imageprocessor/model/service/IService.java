package com.cherepanov.imageprocessor.model.service;

import okhttp3.ResponseBody;
import rx.Observable;

public interface IService {

    Observable<ResponseBody> loadImage(String url);
}
