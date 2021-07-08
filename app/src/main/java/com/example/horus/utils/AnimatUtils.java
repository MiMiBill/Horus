package com.example.horus.utils;

import android.view.View;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;

public class AnimatUtils {

    public static void addSpringAnim(View view){
//        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view,View.SCALE_X,1f,1.2f,1f);
//        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view,View.SCALE_Y,1f,1.2f,1f);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(600);
//        animatorSet.setInterpolator(new SpringScaleInterpolator(0.9f));
//        animatorSet.playTogether(animatorX,animatorY);
//        animatorSet.start();

        /**
         * 我们需要什么样的需求就重写对应方法就好。
         * 上面的代码中有SpringConfig这个对象，通过看源码发现
         * 他的构造函数接受两个变量：1.tension(拉力)、2.friction(摩擦力)。
         * 作用是什么呢？很好理解tension拉力越大，弹性越大，friction
         * 摩擦力越大，弹性效果越小。默认的tension值，friction值如下：
         */
        SpringSystem springSystem = SpringSystem.create();
        Spring spring = springSystem.createSpring();
        spring.setCurrentValue(0.8f);
        spring.setSpringConfig(new SpringConfig(100,8));
        spring.addListener(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                float currentValue = (float) spring.getCurrentValue();
                view.setScaleX(currentValue);
                view.setScaleY(currentValue);
            }
        });
        spring.setEndValue(1f);

    }
}
