package com.example.horus.ui.main;

import com.example.horus.mvp.IBaseView;


public interface MainContract {

    interface MainView extends IBaseView {

        void createVideoCallOrderSuccess();

        void createVideoCallOrderFailed(int code);

    }

    interface IMain {

        void createGrapChatVideoCallOrder();
    }

}
