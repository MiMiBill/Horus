package com.example.horus.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.horus.R;


/**
 * des:
 * date: 2019-05-09 11:00
 */
public class LoadDataFlyView extends FrameLayout {
    private AnimationDrawable mAnimationDrawable;
    private Context mContext;
    public LoadDataFlyView(@NonNull Context context) {
        this(context, null);
    }

    public LoadDataFlyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }



    public LoadDataFlyView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mContext=context;


        LayoutInflater.from(context).inflate(R.layout.wight_data_loading_fly, this, true);
        setAnimationDrawable(R.drawable.loading_data_fly);
        setBackgroundResource(R.color.gray_70747C);


    }


    public void setAnimationDrawable(int res){

        mAnimationDrawable = (AnimationDrawable) mContext.getResources().getDrawable(res);
        ((ImageView) findViewById(R.id.img)).setImageDrawable(mAnimationDrawable);


    }


    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAnimationDrawable != null) {
            mAnimationDrawable.start();
        }
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
    }
}
