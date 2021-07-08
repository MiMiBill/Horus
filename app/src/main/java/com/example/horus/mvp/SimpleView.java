package com.example.horus.mvp;


public interface SimpleView extends BaseView {

    @Override
    default void showProgressView() {

    }

    @Override
    default void hideProgressView() {

    }

    @Override
    default void showToast(String msg) {

    }

    @Override
    default void showDataError() {

    }
}
