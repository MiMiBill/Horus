package com.example.horus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

/**
 * des: RCV 工具类
 */
public class RcvUtils {

    /**
     * 关闭item动画
     */
    public static void closeItemChangeAnimations(RecyclerView recyclerView) {
        SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) recyclerView.getItemAnimator();
        if (simpleItemAnimator != null) {
            simpleItemAnimator.setSupportsChangeAnimations(false);
        }

    }


    /**
     * 控制Glide 的加载
     * 滚动的时候暂停加载
     */
    public static void controlGlideRequests(RecyclerView recyclerView){
        Context context=recyclerView.getContext();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Activity activity=ActivityUtils.getActivityFromView(recyclerView);
                    if(activity!=null&&!activity.isDestroyed()){
                        Glide.with(context).resumeRequests();
                    }

                }else {
                    Glide.with(context).pauseRequests();
                }
            }
        });

    }


    /**
     * https://muyangmin.github.io/glide-docs-cn/int/recyclerview.html
     * 预加载
     * 好像不支持Rcv inside Rcv
     */
    public static void addPreloadProvider(RecyclerView recyclerView){
        Resources resources= recyclerView.getContext().getResources();


    }


}
