package com.whaley.core.utils;

import com.orhanobut.logger.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author: qxw
 * Date: 2016/9/5
 * 与时间有关的转化的工具類
 */
public class DateUtils {

    public final static String YYYYMMDD_NYR = "yyyy年MM月dd日";


    /**
     * 判断给定时间搓字符串是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        Date time = toDate(sdate);
        return isToday(sdate);
    }

    /**
     * 判断给定时间搓是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(long sdate) {
        Date time = toDate(sdate);
        return isToday(time);
    }

    /**
     * 判断给定时间是否为今日
     *
     * @param time
     * @return boolean
     */
    public static boolean isToday(Date time) {
        boolean isToday = false;
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormaterYearMonthDay.get().format(today);
            String timeDate = dateFormaterYearMonthDay.get().format(time);
            if (nowDate.equals(timeDate)) {
                isToday = true;
            }
        }
        return isToday;
    }

    /**
     * 判断给定时间搓是否为明日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isTomorrow(long sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormaterYearMonthDay.get().format(today);
            String timeDate = dateFormaterYearMonthDay.get().format(time);
            String[] days = nowDate.split("-");
            String[] liveTime = timeDate.split("-");
            if (days[1].equals(liveTime[1])) {
                if (Integer.valueOf(liveTime[2]) - Integer.valueOf(days[2]) == 1) {
                    b = true;
                }
            }
        }
        return b;
    }


    /**
     * 判断给定字符串距离当前时间还有几天
     *
     * @param sdate
     * @return boolean
     */
    public static int timeRemain(String sdate) {
        Date today = new Date();
        long sdateLong = Long.parseLong(sdate);
        if (today.getTime() > sdateLong) {
            return -1;
        }
        Date time = toDate(sdateLong);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTime(time);
        long sdateTime = calendar.getTimeInMillis();
        calendar.setTime(today);
        long todayTime = calendar.getTimeInMillis();
        int days = (int) ((sdateTime - todayTime) / (1000 * 60 * 60 * 24));
        if (isToday(sdateTime - days * (1000 * 60 * 60 * 24))) {
            return days;
        } else {
            return days + 1;
        }
    }

    public static int timeDistance(long sdate) {
        Date today = new Date();
        if (sdate > today.getTime()) {
            return 0;
        }
        Date time = toDate(sdate);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTime(time);
        long sdateTime = calendar.getTimeInMillis();
        calendar.setTime(today);
        long todayTime = calendar.getTimeInMillis();
        int days = (int) ((sdateTime - todayTime) / (1000 * 60 * 60 * 24));
        if (isToday(sdateTime - days * (1000 * 60 * 60 * 24))) {
            return days;
        } else {
            return days - 1;
        }
    }

    /**
     * 给定时间已经过去了多久
     *
     * @param sdate
     * @return boolean
     */
    public static int timeDistance(String sdate) {
        long sdateLong = Long.parseLong(sdate);
        return timeDistance(sdateLong);
    }

    public static String foramteToYY(String sdate) {
        return dateFormaterYY.get().format(toDate(sdate));
    }

    /**
     * @author qxw
     * @time 2016/9/5 21:30
     * 转化为yyyy-MM-dd形式的时间格式
     */
    public static String foramteToYYYYMMDD(String sdate) {
        return dateFormaterYearMonthDay.get().format(toDate(sdate));
    }

    public static String foramteToMMDD(String sdate) {
        Date time = toDate(sdate);
        if (time != null) {
            return dateFormaterMonthDay.get().format(toDate(sdate));
        } else {
            return "";
        }
    }

    /**
     * @author qxw
     * @time 2016/9/5 21:30
     * 转化为HH:mm形式的时间格式
     */
    public static String foramteToHHMM(String sdate) {
        Date time = toDate(sdate);
        if (time != null) {
            return dateFormaterDivision.get().format(time);
        } else {
            return "";
        }

    }

    /**
     * @author qxw
     * @time 2016/9/5 21:30
     * 转化为HH:mm形式的时间格式
     */
    public static String foramteToHHMM(long sdate) {
        Date time = toDate(sdate);
        if (time != null) {
            return dateFormaterDivision.get().format(time);
        } else {
            return "";
        }

    }

    /**
     * @author qxw
     * @time 2016/9/7 22:30
     * 转化为yyyy.MM.dd HH:mm形式的时间格式
     */
    public static String foramteToYYYYMMDDHHMM(String sdate) {
        Date time = toDate(sdate);
        if (time != null) {
            return dateFormaterYearMonthDays.get().format(time);
        } else {
            return "";
        }

    }

    /**
     * @author qxw
     * @time 2016/9/7 22:30
     * 转化为yyyy.MM.dd HH:mm形式的时间格式
     */
    public static String foramteToYYYYMMDDHHMM(long sdate) {
        Date time = toDate(sdate);
        if (time != null) {
            return dateFormaterYearMonthDays.get().format(time);
        } else {
            return "";
        }
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormaterCompleteTime.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toDate(long sdate) {
        try {
            return new Date(sdate);
        } catch (Exception e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormaterYearMonthDay = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormaterMonthDay = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM-dd");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormaterCompleteTime = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormaterDivision = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormaterYearMonthDays = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy.MM.dd HH:mm");
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dateFormaterYY = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        }
    };


    public static String foramteToDate(String sdate, String timeFormat) {
        try {
            long longDate = Long.parseLong(sdate);
            return foramteToDate(longDate, timeFormat);
        } catch (Exception e) {
            return "";
        }
    }


    public static String foramteToDate(long sdate, String timeFormat) {
        Date time = toDate(sdate);
        if (time != null) {
            return new SimpleDateFormat(timeFormat).format(time);
        } else {
            return "";
        }
    }


    /**
     * 描述：标准化日期时间类型的数据，不足两位的补0.
     *
     * @param dateTime 预格式的时间字符串，如:2012-3-2 12:2:20
     * @return String 格式化好的时间字符串，如:2012-03-20 12:02:20
     */
    public static String dateTimeFormat(String dateTime) {
        StringBuilder sb = new StringBuilder();
        try {
            if (StrUtil.isEmpty(dateTime)) {
                return null;
            }
            String[] dateAndTime = dateTime.split(" ");
            if (dateAndTime.length > 0) {
                for (String str : dateAndTime) {
                    if (str.indexOf("-") != -1) {
                        String[] date = str.split("-");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append("-");
                            }
                        }
                    } else if (str.indexOf(":") != -1) {
                        sb.append(" ");
                        String[] date = str.split(":");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(strFormat2(str1));
                            if (i < date.length - 1) {
                                sb.append(":");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(e, "dateTimeFormat");
            return null;
        }
        return sb.toString();
    }

    /**
     * 描述：不足2个字符的在前面补“0”.
     *
     * @param str 指定的字符串
     * @return 至少2个字符的字符串
     */
    public static String strFormat2(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            Logger.e(e, "strFormat2");
        }
        return str;
    }
}
