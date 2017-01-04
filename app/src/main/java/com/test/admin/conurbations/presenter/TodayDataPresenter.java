package com.test.admin.conurbations.presenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by zhouqiong on 2016/12/12.
 */

public class TodayDataPresenter {


    public TodayDataPresenter() {

    }

    public void getTodayData() {
        for (int i = 12; i < 17; i++) {
           /* MyRequest.getInstance().requestUrl(RequestUrl.URL_DATA_TODAY + i, new RequestCallback() {
                @Override
                public void onResponse(Object o, int i) {
                    todayDataDAO.jsonToAllResource(o);
                    iTodayDataList.setTodayData(todayDataDAO.getToadyData());
                    todayDataDAO.clear();
                }
            });*/
        }
    }

    private String getData(int i) {
        Date date = new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, -i);//把日期往后增加一天.整数往后推,负数往前移动
        if (!checkHoliday(calendar)){
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(date);
    }

    private boolean checkHoliday(Calendar src) {
        if (src.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || src.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        }
        return false;
    }
}
