package com.example.horus.ui.main;

import androidx.lifecycle.LifecycleOwner;

import com.alibaba.fastjson.JSONObject;
import com.example.horus.mvp.WrapMvpBasePresenter;


/**

 */
public class MainPresenter extends WrapMvpBasePresenter<MainContract.MainView> implements MainContract.IMain {
    private static final String TAG = MainPresenter.class.getSimpleName();


    public MainPresenter(LifecycleOwner provider) {
        super(provider);
    }


    @Override
    public void createGrapChatVideoCallOrder() {



    }
}
