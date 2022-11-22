package com.javaweb.canteen.common;

import cn.hutool.core.date.DateUtil;

import java.util.Date;

/**
 * 业务的时间处理工具类
 */
public class MyTimeUtils {

    /**
     *  获取当前时间的一周的开始
     */
    public static Date getWeekOfBeginTime() {
        // 获取当前时间是这周第几天
        int thisDayOfWeek = DateUtil.thisDayOfWeek();
        Date weekOfBeginTime;
        if (thisDayOfWeek == 1) {   // 是否是周日
            // 偏移到七天前即本周周一并获取这一天的开始位置
            weekOfBeginTime = DateUtil.beginOfDay(DateUtil.offsetDay(DateUtil.date(),(DateUtil.thisDayOfWeek()-7)));
        }else{
            weekOfBeginTime = DateUtil.beginOfDay(DateUtil.offsetDay(DateUtil.date(),(2 - DateUtil.thisDayOfWeek())));
        }
        return weekOfBeginTime;
    }

    /**
     *  获取当前时间的一周的结尾
     */
    public static Date getWeekOfEndTime() {
        // 获取当前时间是这周第几天
        int thisDayOfWeek = DateUtil.thisDayOfWeek();
        Date weekOfEndTime;
        if (thisDayOfWeek == 1) {   // 是否是周日
            weekOfEndTime = DateUtil.endOfDay(DateUtil.date());
        }else{
            weekOfEndTime = DateUtil.endOfDay(DateUtil.offsetDay(DateUtil.date(),(8 - DateUtil.thisDayOfWeek())));
        }
        return weekOfEndTime;
    }

    /**
     *  获取当前时间的下一周的开始
     */
    public static Date getNextWeekOfBeginTime() {
        // 获取当前时间的这一周的开始
        Date weekOfBeginTime = getWeekOfBeginTime();
        return DateUtil.offsetWeek(weekOfBeginTime, 1);
    }

    /**
     *  获取当前时间的下一周的结尾
     */
    public static Date getNextWeekOfEndTime() {
        // 获取当前时间的这一周的结尾
        Date weekOfEndTime = getWeekOfEndTime();
        return DateUtil.offsetWeek(weekOfEndTime, 1);
    }

    /**
     * 通过yyyy-MM-01获取当前所在月份的开始时间
     */
    public static Date getMonthOfBeginTime(String date) {
        Date month = DateUtil.parse(date);
        return DateUtil.beginOfDay(month);
    }

    /**
     * 通过yyyy-MM-01获取当前所在月份的终点时间
     */
    public static Date getMonthOfEndTime(String date) {
        Date month = DateUtil.parse(date);
        return DateUtil.endOfDay(DateUtil.offsetDay(DateUtil.offsetMonth(month, 1), -1));
    }

    /**
     * 通过字符串date获取对应时间的当天开始时间
     */
    public static Date getDayBeginOfTime(String date){
        Date day = DateUtil.parse(date);
        return DateUtil.beginOfDay(day);
    }

    /**
     * 通过字符串date获取对应时间的当天终点时间
     */
    public static Date getDayEndOfTime(String date){
        Date day = DateUtil.parse(date);
        return DateUtil.endOfDay(DateUtil.offsetDay(DateUtil.offsetMonth(day, 1), -1));
    }

    /**
     * 根据当前时间获取下周对应时间
     */
    public static Date getNextWeekOfTime(Date date){
        return DateUtil.nextWeek();
    }
}
