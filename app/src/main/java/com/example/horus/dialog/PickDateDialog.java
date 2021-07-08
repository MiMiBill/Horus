package com.example.horus.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * 选择日期窗口
 *
 * Created by Liao on 2017/3/20
 */

public class PickDateDialog implements DatePickerDialog.OnDateSetListener,
        DialogInterface.OnClickListener{

    private static final String TAG = PickDateDialog.class.getSimpleName();
    private Context context;

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    private OnDateClickListener dateClickListener;

    private Calendar minCalendar;
    private Calendar maxCalendar;

    private ArrayList<Integer> whichBtnArr;
    private ArrayList<String> btnTextArr;
    private ArrayList<OnPickDateClickListener> clickListeners;

    private boolean touchOutsideCancelable;
    private boolean cancelable;

    private boolean closeable;

    public static final int TIME_NOW   = 0;// 当天当前时间
    public static final int TIME_START = 1;// 当天最大时间（23:59:59:999）
    public static final int TIME_END   = 2;// 当天最小时间（00:00:00:000）

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (clickListeners != null && whichBtnArr != null) {
            int index = whichBtnArr.indexOf(which);
            if (index > -1 && clickListeners.size() > index && clickListeners.get(index) != null) {
                clickListeners.get(index).onPickDateClick(this, which);
            } else {
                Log.e(TAG, "Illegal index of ClickListeners");
            }
        }
    }

    public interface OnPickDateClickListener {
        void onPickDateClick(PickDateDialog dialog, int which);
    }

    public interface OnDateClickListener {
        void onDateClick(PickDateDialog dialog, Calendar calendar);
    }

    // 设置点击按钮后是否自动关闭
    public void setAfterClickCloseable(boolean closeable) {
        try {
            Field field = datePickerDialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(datePickerDialog, closeable);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDateClickListener(OnDateClickListener listener) {
        dateClickListener = listener;
    }

    public PickDateDialog(Context context) {
        this.context = context;

        whichBtnArr = new ArrayList<>();
        btnTextArr = new ArrayList<>();
        clickListeners = new ArrayList<>();

        minCalendar = null;
        maxCalendar = null;

        datePickerDialog = null;

        touchOutsideCancelable = true;
        cancelable = true;
    }

    public void setCanceledOnTouchOutside(boolean flag) {
        touchOutsideCancelable = false;
    }

    public void setCancelable(boolean flag) {
        cancelable = flag;
    }

    public void setButton(int whichButton, String btnText, OnPickDateClickListener listener) {
        whichBtnArr.add(whichButton);
        btnTextArr.add(btnText);
        clickListeners.add(listener);
    }

    public void setMaxDate(Calendar maxCalendar) {
        this.maxCalendar = maxCalendar;
    }

    public void setMaxDate(int year, int month, int date) {
        maxCalendar.set(year, month, date);
    }

    public void setMinDate(Calendar minCalendar) {
        this.minCalendar = minCalendar;
    }

    public void setMinDate(int year, int month, int date) {
        minCalendar.set(year, month, date);
    }

    public void showDialog() {
        Calendar calendar = Calendar.getInstance();
        this.showDialog(calendar);
    }

    public void showDialog(Calendar selected) {
        int year = selected.get(Calendar.YEAR);
        int month = selected.get(Calendar.MONTH);
        int date = selected.get(Calendar.DAY_OF_MONTH);
        this.showDialog(year, month, date);
    }

    public void showDialog(int year, int month, int date) {
        // 每一次show都是一次初始化
        datePickerDialog = new DatePickerDialog(context, this, year, month, date);

        // 设置最大值最小值
        if (minCalendar != null) {
            minCalendar = resetTime(minCalendar, TIME_START);
            datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());
        }
        if (maxCalendar != null) {
            maxCalendar = resetTime(maxCalendar, TIME_END);
            datePickerDialog.getDatePicker().setMaxDate(maxCalendar.getTimeInMillis());
        }

        // 设置点击事件
        for (int i = 0; i < whichBtnArr.size(); i++) {
            datePickerDialog.setButton(whichBtnArr.get(i), btnTextArr.get(i), this);
        }

        datePickerDialog.setCanceledOnTouchOutside(touchOutsideCancelable);
        datePickerDialog.setCancelable(cancelable);

        datePickerDialog.show();
    }

    public void closeDialog() {
        if (datePickerDialog != null && datePickerDialog.isShowing()) {
            datePickerDialog.dismiss();
        }
    }

    public Calendar getCalendar() {
        return getCalendar(TIME_NOW);
    }

    public Calendar getCalendar(int flag) {
        int year = datePickerDialog.getDatePicker().getYear();
        int month = datePickerDialog.getDatePicker().getMonth();
        int date = datePickerDialog.getDatePicker().getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, date);

        calendar = resetTime(calendar, flag);

        return calendar;
    }

    private Calendar getCalendar(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar;
    }

    private Calendar resetTime(Calendar calendar, int flag) {
        if (flag == TIME_START) {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        } else if (flag == TIME_END) {
            calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
            calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
            calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
            calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        }
        return calendar;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

}
