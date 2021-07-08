package com.example.horus.mvp;


public interface BaseView {

    void showProgressView();

    void hideProgressView();

    void showToast(String msg);

    void showDataError();
}
