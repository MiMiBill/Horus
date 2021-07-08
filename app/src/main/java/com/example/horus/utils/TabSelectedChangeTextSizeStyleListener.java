package com.example.horus.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.horus.R;

/**
 * des:
 */
public class TabSelectedChangeTextSizeStyleListener implements TabLayout.OnTabSelectedListener {


    private Context mContext;
    private TabLayout mTabLayout;

    private int mSelectStyle;
    private int mUnSelectStyle;

    public TabSelectedChangeTextSizeStyleListener(TabLayout tabLayout) {
        this(tabLayout, R.style.HomeTabTextAppearance_Selected, R.style.HomeTabTextAppearance,0);
    }


    public TabSelectedChangeTextSizeStyleListener(TabLayout tabLayout, int selectStyle, int unSelectStyle,int defaultSelectPosition) {
        mContext = tabLayout.getContext();
        mTabLayout = tabLayout;
        mSelectStyle=selectStyle;
        mUnSelectStyle=unSelectStyle;

        //选中 默认的item
        mTabLayout.post(() -> {
            Activity activity = ActivityUtils.getActivityFromView(mTabLayout);
            if (activity != null && !activity.isFinishing()) {
                selectItem(defaultSelectPosition, mSelectStyle,true);
            }
        });

    }


    @Override public void onTabSelected(TabLayout.Tab tab) {
        selectItem(tab.getPosition(), mSelectStyle,true);

    }

    @Override public void onTabUnselected(TabLayout.Tab tab) {
        selectItem(tab.getPosition(), mUnSelectStyle,false);
    }

    @Override public void onTabReselected(TabLayout.Tab tab) {

    }

    private void selectItem(int i, int homeTabTextAppearance_selected,boolean selected) {


        ViewGroup viewGroup = (ViewGroup) mTabLayout.getChildAt(0);
        ViewGroup group = (ViewGroup) viewGroup.getChildAt(i);
        TextView textView = (TextView) group.getChildAt(1);

        textView.setTypeface(Typeface.defaultFromStyle(selected?Typeface.BOLD:Typeface.NORMAL));


        SpannableString spannableString = new SpannableString(textView.getText());
        spannableString.setSpan(new TextAppearanceSpan(mContext, homeTabTextAppearance_selected),
                0, textView.getText().length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }
}
