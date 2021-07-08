package com.example.horus.mvp;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 *
 */
public interface IBaseView extends MvpView {

    void showProgressView();

    void hideProgressView();

    void showToast(String msg);


}
