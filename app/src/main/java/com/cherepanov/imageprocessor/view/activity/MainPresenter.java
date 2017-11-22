package com.cherepanov.imageprocessor.view.activity;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

/**
 * Created by Денис on 23.11.2017.
 */
class MainPresenter extends MvpBasePresenter<MainView> {

    private boolean isCheck = false;

    public void checkPermissions(){
        if (isViewAttached() && !isCheck){
            getView().checkPermission();
        }
        isCheck = true;
    }
}
