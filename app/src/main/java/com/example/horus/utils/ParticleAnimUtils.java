package com.example.horus.utils;

import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;
import com.example.horus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * des: 粒子动画
 */
public class ParticleAnimUtils {


    private static final long DURATION = 600;
    private static final int[] LIKE_RES = new int[]{
            R.mipmap.moments_like_one, R.mipmap.moments_like_two, R.mipmap.moments_like_three,
            R.mipmap.moments_like_four, R.mipmap.moments_like_five, R.mipmap.moments_like_six,
            R.mipmap.moments_like_seven, R.mipmap.moments_like_eight, R.mipmap.moments_like_nine,
            R.mipmap.moments_like_ten, R.mipmap.moments_like_eleven, R.mipmap.moments_like_twelve,
            R.mipmap.moments_like_thirteen, R.mipmap.moments_like_fourteen, R.mipmap.moments_like_fifteen,

    };


    /**
     * 个人中心say hi 动画
     */
    private static final int[] SAY_HI_RES = new int[]{
            R.mipmap.personal_say_hi_anim_0, R.mipmap.personal_say_hi_anim_1, R.mipmap.personal_say_hi_anim_2,
            R.mipmap.personal_say_hi_anim_3, R.mipmap.personal_say_hi_anim_4, R.mipmap.personal_say_hi_anim_5,
            R.mipmap.personal_say_hi_anim_6, R.mipmap.personal_say_hi_anim_7
    };

    /**
     * 动态点赞的效果
     *
     * @param anchor 要依赖的view
     *
     *               由于创建的ParticleSystem对象是和Activity绑定的 所以每次都要创建新粒子
     *               否则Activity finish掉再次进入时就无法播放粒子了
     */
    public static void showMomentsLikeAnim(Activity activity, View anchor) {
        List<ParticleSystem> particleList = new ArrayList<>();

        for (int res : LIKE_RES) {
            particleList.add(getParticleSystemLike(activity, res));
        }

        //只发射一次
        for (ParticleSystem item : particleList) {
            item.oneShot(anchor, 1);
        }

        ObjectAnimator animatorScaleX=ObjectAnimator.ofFloat(anchor,View.SCALE_X,0.3f,1.2f,1f);
        ObjectAnimator animatorScaleY=ObjectAnimator.ofFloat(anchor,View.SCALE_Y,0.3f,1.2f,1f);


        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animatorScaleX,animatorScaleY);
        animatorSet.setDuration(DURATION);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
    }


    private static ParticleSystem getParticleSystemLike(Activity activity, int res) {
        return new ParticleSystem(activity, 1, res, DURATION)
                .setSpeedModuleAndAngleRange(0.2f, 0.8f, 0, 360)
                .setRotationSpeed(144)
                .setScaleRange(1.2f,2.4f)
                .setAcceleration(0.0009f, 90);
    }

    public static void showSayHiAnim(Activity activity, View anchor,AnimatorListenerAdapter animatorListenerAdapter) {

        List<ParticleSystem> particleList = new ArrayList<>();

        for (int res : SAY_HI_RES) {
            particleList.add(getParticleSystemSayHi(activity, res));
        }

        //只发射一次
        for (ParticleSystem item : particleList) {
            item.oneShot(anchor, 1);
        }

        ObjectAnimator animatorScaleX=ObjectAnimator.ofFloat(anchor,View.SCALE_X,0.3f,1.2f,1f);
        ObjectAnimator animatorScaleY=ObjectAnimator.ofFloat(anchor,View.SCALE_Y,0.3f,1.2f,1f);


        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animatorScaleX,animatorScaleY);
        animatorSet.setDuration(DURATION);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.addListener(animatorListenerAdapter);
        animatorSet.start();
    }

    private static ParticleSystem getParticleSystemSayHi(Activity activity, int res) {
        return new ParticleSystem(activity, 1, res, DURATION)
                .setSpeedModuleAndAngleRange(0.4f, 0.8f, 180, 350)
                .setRotationSpeed(220)
                .setScaleRange(1.2f,2.4f)
                .setAcceleration(-0.0009f, 270);
    }

}
