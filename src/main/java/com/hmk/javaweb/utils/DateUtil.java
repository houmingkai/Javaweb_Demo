package com.hmk.javaweb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 时间日期处理工具类
 *
 */
@SuppressWarnings("all")
public final class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 日期格式化对象
     */
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static DateFormat dateFormat_yy = new SimpleDateFormat("yy-MM-dd");

    private static DateFormat dateFormat_slide = new SimpleDateFormat("yyyy/MM/dd");

    private static DateFormat dateFormat_input = new SimpleDateFormat("yyyyMMdd");
    /**
     * 日期时间格式化对象
     */
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static DateFormat dateTimeFormatS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static DateFormat dateTimeFormat_input = new SimpleDateFormat("yyyyMMddHHmm");
    /**
     * 时间格式化对象
     */
    private static DateFormat timeFormat = new SimpleDateFormat("HH:mm");

    /**
     * 时间格式化对象
     */
    private static DateFormat timeSecFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 获取时间格式化对象 "yyyy-MM-dd"
     *
     * @return
     */
    public static final DateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * 获取时间格式化对象 "yy-MM-dd"
     *
     * @return
     */
    public static DateFormat getDateFormat_yy() {
        return dateFormat_yy;
    }

    /**
     * 日期输入格式采用"yyyyMMdd"
     *
     * @return
     */
    public static final DateFormat getDateFormat_input() {
        return dateFormat_input;
    }

    /**
     * 获取时间日期格式化对象 "yyyy-MM-dd HH:mm"
     *
     * @return
     */
    public static final DateFormat getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**
     * 获取当前时间的时间对象
     *
     * @return
     */
    public static final Date nowDate() {
        return new Date();
    }

    /**
     * 系统最小时间
     *
     * @return
     */
    public static final Date minDate() {
        return dateBegin(getDate(1900, 1, 1));
    }

    /**
     * 系统最大时间
     *
     * @return
     */
    public static final Date maxDate() {
        return dateEnd(getDate(2079, 1, 1));
    }

    /**
     * 获取指定时间的年
     *
     * @param date
     * @return
     */
    public static final int year(Date date) {
        if (date == null) return 0;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取指定时间的月
     *
     * @param date
     * @return
     */
    public static final int month(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取指定时间的日
     *
     * @param date
     * @return
     */
    public static final int day(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取一个时间对象
     *
     * @param year  格式为：2004
     * @param month 从1开始
     * @param date  从1开始
     * @return
     */
    public static final Date getDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date);
        return calendar.getTime();
    }

    /**
     * 获取一个时间对象
     *
     * @param year   格式为：2004
     * @param month  从1开始
     * @param date   从1开始
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static final Date getDateTime(int year, int month, int date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, date, hour, minute, second);
        return calendar.getTime();
    }

    /**
     * 在一个已知时间的基础上增加指定的时间,负数表示减少
     *
     * @param oldDate
     * @param year
     * @param month
     * @param date
     * @return
     */
    public static final Date addDate(Date oldDate, int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DATE, date);
        return calendar.getTime();
    }

    /**
     *  获取三个月前的开始时间
     * @return
     */
    public static String threeMAgoBegin(){
        Date date = dateBegin(DateUtil.addDate(new Date(), 0, -3, 0));
        return DateUtil.formatDateTimeS(date);
    }


    public static int constDateSub = -36500;

    /**
     * 返回两个时间相差的天数
     *
     * @param a
     * @param b
     * @return
     */
    public static final int dateSub(Date a, Date b) {
        if (a == null || b == null) {
            return constDateSub;
        }

        int date = (int) (a.getTime() / (24 * 60 * 60 * 1000) - b.getTime() / (24 * 60 * 60 * 1000));
        return date;
    }

    public static final int dateSubAddOne(Date a, Date b) {
        int date = (int) (a.getTime() / (24 * 60 * 60 * 1000) - b.getTime() / (24 * 60 * 60 * 1000));
        return date <= 0 ? 1 : date + 1;
    }

    public static final boolean isBetweenDateS(Date beginDate, Date nowDate, Date endDate) {
        if (beginDate != null && nowDate != null && endDate != null) {
            return (beginDate.getTime() / (24 * 60 * 60 * 1000)) <= (nowDate.getTime() / (24 * 60 * 60 * 1000))
                    && (nowDate.getTime() / (24 * 60 * 60 * 1000)) <= (endDate.getTime() / (24 * 60 * 60 * 1000));
        } else if (beginDate != null && nowDate != null && endDate == null) {
            return (beginDate.getTime() / (24 * 60 * 60 * 1000)) <= (nowDate.getTime() / (24 * 60 * 60 * 1000));
        } else if (beginDate == null && nowDate != null && endDate != null) {
            return (nowDate.getTime() / (24 * 60 * 60 * 1000)) <= (endDate.getTime() / (24 * 60 * 60 * 1000));
        }
        return false;
    }

    /**
     * 返回两个时间相差多少秒
     *
     * @param a
     * @param b
     * @return
     */
    public static final int subSecond(Date a, Date b) {

        return (int) (a.getTime() / (1000) - b.getTime() / (1000));
    }

    public static final int subSecond(String str, Date b) {
        Date a = null;
        try {
            a = timeFormat.parse(str);
        } catch (ParseException e) {

            return 0;
        }
        return (int) ((a.getTime() % (24 * 60 * 60 * 1000)) / 1000 - (b.getTime() % (24 * 60 * 60 * 1000)) / 1000);
    }



    /**
     *  获取最大时间
     * @param dateList
     * @return
     */
    public static Date getMaxTime(List<String> dateList){
        List<Long> list = new ArrayList<>();
        for (String dateStr : dateList) {
            list.add(dateToTimestamp(dateStr));
        }
        String maxTime = timestampToDate(Collections.max(list));
        return  parseDateTimes(maxTime);
    }

    public static void main(String[] args){
//        List<String> dateList = new ArrayList<>();
//        dateList.add("2019-09-20 12:34:09");
//        dateList.add("2019-09-25 12:34:09");
//        dateList.add("2019-09-22 12:34:09");
//        dateList.add("2019-09-23 12:34:09");
//
//        System.out.println(formatDateTimeS(getMaxTime(dateList)));

//        String s = DateUtil.formatDateTimeS(DateUtil.dateEnd(DateUtil.addDate(new Date(), 0, 1, 30)));
//        System.out.println(s);
//        System.out.println(subSecond(new Date(),parseDateTimes("2019-10-19 15:12:18")));
        int remDays = DateUtil.dateSub(new Date(), DateUtil.parseDateTimes("2019-11-16 23:59:59"));
        System.out.println(remDays);

    }

    /**
     *  时间字符串转时间戳
     * @param time
     * @return
     */
    public static long dateToTimestamp(String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(time);
            long ts = date.getTime()/1000;
            return ts;
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 时间戳转时间字符串
     * @param time
     * @return
     */
    public static String timestampToDate(long time) {
        if (time < 10000000000L) {
            time = time * 1000;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(time))));
        return sd;
    }


    /**
     * 一天的开始时间
     *
     * @param date
     * @return
     */
    public static final Date dateBegin(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dateBegin(calendar);
        return calendar.getTime();
    }

    /**
     * 一天的结束时间
     *
     * @param date
     * @return
     */
    public static final Date dateEnd(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        dateEnd(calendar);
        return calendar.getTime();
    }

    /**
     * 一天的结束时间
     *
     * @param calendar
     * @return
     */
    public static final Calendar dateEnd(Calendar calendar) {
        if (calendar == null) return null;
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 一天的开始时间
     *
     * @param calendar
     * @return
     */
    public static final Calendar dateBegin(Calendar calendar) {
        if (calendar == null) return null;
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * 一月的开始时间
     *
     * @param date
     * @return
     */
    public static final Date monthBegin(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DATE, day);
        dateBegin(calendar);
        return calendar.getTime();
    }

    /**
     * 一月的结束时间
     *
     * @param date
     * @return
     */
    public static final Date monthEnd(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DATE, day);
        dateEnd(calendar);
        return calendar.getTime();
    }

    /**
     * 一年的开始时间
     *
     * @param date
     * @return
     */
    public static final Date yearBegin(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.getActualMinimum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DATE, month);
        dateBegin(calendar);
        return calendar.getTime();
        // return parseDate(formatDate(date).substring(0,4)+"-01-01");
    }

    /**
     * 一年的结束时间
     *
     * @param date
     * @return
     */
    public static final Date yearEnd(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
        calendar.set(Calendar.DATE, day);
        dateEnd(calendar);
        return calendar.getTime();
        // return parseDate(formatDate(date).substring(0,4)+"-12-31");
    }

    /**
     * 从字符串转换为date 默认格式为 "yyyy-MM-dd"
     *
     * @param source
     * @return
     */
    public static final Date parseDate(String source) {
        if (source == null || source.trim().length() == 0) return null;
        if (source.trim().length() == 8) try {
            Date returnDate = dateFormat_input.parse(source);
            return returnDate;
        } catch (ParseException e) {
            logger.error("DateUtil parseDate error", e);
            return null;
        }

        try {
            Date returnDate = dateFormat.parse(source);
            return returnDate;
        } catch (ParseException e) {
            logger.error("DateUtil parseDate error", e);
            return null;
        }
    }

    /**
     * 从字符串转换为date 默认格式为 "yyyy-MM-dd HH:mm"
     *
     * @param source
     * @return
     */
    public static final Date parseDateTime(String source) {
        if (source == null || source.length() == 0) return null;
        try {
            return dateTimeFormat.parse(source);
        } catch (ParseException e) {
            logger.error("DateUtil parseDate error", e);
            return null;
        }
    }

    /**
     * 从字符串转换为date 默认格式为 "yyyy-MM-dd HH:mm:ss"
     *
     * @param source
     * @return
     */
    public static final Date parseDateTimes(String source) {
        if (source == null || source.equals("") || source.length() == 0) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(source);
        } catch (ParseException e) {
            logger.error("DateUtil parseDate error", e);
        }
        return null;
    }

    /**
     * 格式化输出（只读的时候） 默认格式为 "yyyy-MM-dd"
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if (date == null) return "";
        return dateFormat.format(date);
    }

    /**
     * 格式化输出（只读的时候） 默认格式为 "yy-MM-dd"
     *
     * @param date
     * @return
     */
    public static String formatDateYY(Date date) {
        if (date == null) return "";
        return dateFormat_yy.format(date);
    }

    /**
     * 格式化输出显示（填写的时候） yyyyMMdd
     *
     * @param date
     * @return
     */
    public static String formatDate_input(Date date) {
        if (date == null) return "";
        return dateFormat_input.format(date);
    }

    /**
     * 格式化输出显示（填写的时候） yyyy/MM/dd
     *
     * @param date
     * @return
     */
    public static String formatDate_slide(Date date) {
        if (date == null) return "";
        return dateFormat_slide.format(date);
    }

    /**
     * 格式化输出 默认格式为 "yyyy-MM-dd HH:mm"
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return dateTimeFormat.format(date);
    }

    /**
     * 格式化输出 默认格式为 "yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     */
    public static String formatDateTimeS(Date date) {
        if (date == null) return "";
        return dateTimeFormatS.format(date);
    }

    /**
     * 格式化输出 默认格式为 "yyyy-MM-dd HH:mm"
     *
     * @param date
     * @return
     */
    public static String getDateTime(Date date) {
        if (date == null) return "";
        return dateTimeFormat.format(date).substring(5, 10).replaceAll("_", "/");
    }

    public static String formatDateTime_input(Date date) {
        if (date == null) return "";
        return dateTimeFormat_input.format(date);
    }

    /**
     * 按照指定格式将时间转化为str
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDateTime_input(Date date, String format) {
        if (date == null) return "";
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 判断是否是闰年
     *
     * @param yearInt
     * @return
     */
    public static boolean isLeapYear(int yearInt) {
        boolean flag = false;
        if (((yearInt % 4 == 0) && (yearInt % 100 != 0)) || ((yearInt % 4 == 0) && (yearInt % 400 == 0))) {
            return true;
        }
        return flag;
    }

    /**
     * 在指定的时间内增加天数。负数表示为减
     *
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Date newdate = new Date();
        long newtimems = date.getTime() + ((long) days * 24 * 60 * 60 * 1000);
        newdate.setTime(newtimems);
        return newdate;
    }

    /**
     * 根据日期判断今天 昨天 前天3个时间段，如果不是返回String类型
     *
     * @param date
     * @return
     */
    public static String cnDate(Date date) {
        if (date == null) {
            return "";
        }
        Date newdate = new Date();
        Long newTimes = newdate.getTime();
        Long cdTimes = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String dateStr1 = DateUtil.formatDate(DateUtil.addDays(newdate, -1)) + " 23:59:59";
        String dateStr2 = DateUtil.formatDate(DateUtil.addDays(newdate, -2)) + " 23:59:59";
        String dateStr3 = DateUtil.formatDate(DateUtil.addDays(newdate, -3)) + " 23:59:59";
        Date date1 = DateUtil.parseDateTimes(dateStr1);
        Date date2 = DateUtil.parseDateTimes(dateStr2);
        Date date3 = DateUtil.parseDateTimes(dateStr3);
        if (newTimes >= cdTimes && cdTimes > date1.getTime()) {
            return "今天 " + sdf.format(date);
        } else if (cdTimes < date1.getTime() && cdTimes > date2.getTime()) {
            return "昨天 " + sdf.format(date);
        } else if (cdTimes < date2.getTime() && cdTimes > date3.getTime()) {
            return "前天 " + sdf.format(date);
        } else

            return DateUtil.formatDateTime(date);
    }

    /**
     * 判断8位的日期的字符串是否为正确的日期字符串
     *
     * @param dateString
     * @return 不是正确的日期字符串返回true
     */
    public static boolean isErrorFormatDateString(String dateString) {
        boolean flag = false;
        String yearString = "";
        String monthString = "";
        String dayString = "";
        if (dateString.length() == 8) {
            yearString = dateString.substring(0, 4);
            monthString = dateString.substring(4, 6);
            dayString = dateString.substring(6, 8);
        } else {
            return true;
        }
        int yearInt = Integer.parseInt(yearString);
        int monthInt = Integer.parseInt(monthString);
        int dayInt = Integer.parseInt(dayString);
        if (DateUtil.year(DateUtil.nowDate()) != yearInt) {
            return true;
        }
        if (monthInt > 0 && monthInt < 12) {
            switch (monthInt) {
                case 1:
                    if (dayInt > 31 || dayInt < 1) flag = true;
                    break;
                case 2:
                    if (isLeapYear(yearInt)) {
                        if (dayInt > 29 || dayInt < 1) flag = true;
                    } else {
                        if (dayInt > 28 || dayInt < 1) flag = true;
                    }
                    break;
                case 3:
                    if (dayInt > 31 || dayInt < 1) flag = true;
                    break;
                case 4:
                    if (dayInt > 30 || dayInt < 1) flag = true;
                    break;
                case 5:
                    if (dayInt > 31 || dayInt < 1) flag = true;
                    break;
                case 6:
                    if (dayInt > 30 || dayInt < 1) flag = true;
                    break;
                case 7:
                    if (dayInt > 31 || dayInt < 1) flag = true;
                    break;
                case 8:
                    if (dayInt > 31 || dayInt < 1) flag = true;
                    break;
                case 9:
                    if (dayInt > 30 || dayInt < 1) flag = true;
                    break;
                case 10:
                    if (dayInt > 31 || dayInt < 1) flag = true;
                    break;
                case 11:
                    if (dayInt > 30 || dayInt < 1) flag = true;
                    break;
                case 12:
                    if (dayInt > 31 || dayInt < 1) flag = true;
                    break;

                default:
                    break;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 返回秒数对应的分秒或者时分
     *
     * @param type 转换类型：1/分秒 2/时分 例如：1/20分20秒 ，2/1小时/25分钟
     * @param s    秒
     * @return times
     * @throws Exception
     */
    public static String getStrTime(int s, String type) throws Exception {
        String times = "";
        int sec = 1;// second
        int min = 60 * sec;// minute
        int hh = 60 * min;// hour

        // 时、分、秒
        int hour = 0;
        int minute = 0;
        int second = 0;

        // 两位数
        String strH = "";
        String strM = "";
        String strS = "";

        if (type == null) {
            throw new Exception("输入的转换类型为空");
        } else if (type.equals("1")) {
            minute = s / min;
            second = s - (minute * min);
            strM = minute < 10 ? "0" + minute : "" + minute;
            strS = second < 10 ? "0" + second : "" + second;
            times = strM + "分" + strS + "秒";
        } else if (type.equals("2")) {
            hour = s / hh;
            minute = (s - hour * hh) / min;
            strH = hour < 10 ? "0" + hour : "" + hour;
            strM = minute < 10 ? "0" + minute : "" + minute;
            times = strH + "时" + strM + "分";
        }
        return times;
    }

    /**
     * 在一个已知时间的基础上增加指定的时间,负数表示减少
     *
     * @param oldDate
     * @param year
     * @param month
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static final Date addDate(Date oldDate, int year, int month, int date, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldDate);
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DATE, date);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        calendar.add(Calendar.MINUTE, minute);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * 返回“yyyy-MM”格式的String日期
     *
     * @param d “yyyy-MM”
     * @return
     */
    public static String toDateStr(java.util.Date d) {
        if (d == null) {
            return "";
        } else {
            return (new SimpleDateFormat("yyyy-MM")).format(d);
        }
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * sql语句中起始日期处理
     *
     * @param date
     * @return
     */
    public static String sqlDateS(String date) {
        return "str_to_date('" + date + " 00:00:00','%Y-%m-%d %h:%i:%s')";
    }

    /**
     * sql语句中结束日期处理
     *
     * @param date
     * @return
     */
    public static String sqlDateE(String date) {
        return "str_to_date('" + date + " 23:59:59','%Y-%m-%d %h:%i:%s')";
    }

    /**
     * 解析java.util.Date to java.sql.Date(used for ps.setDate(x,xxx))
     *
     * @param dateStr 要转换的java.util.date
     * @return java.sql.Date
     */
    public static java.sql.Date parse2SqlDate(String dateStr) {
        return StringUtil.isBlank(dateStr) ? null : new java.sql.Date(parseDate(dateStr).getTime());
    }

    /**
     * 身份证有效日期效验
     *
     * @param beginStr
     * @param endStr
     * @param birthday
     * @return String 返回1时为效验通过
     * @throws ParseException
     */
    public static String validateDate(String beginStr, String endStr, String birthday) {
        try {
            // 验证日期是否有效
            String regx = "^(?:19|20|30)[0-9][0-9](?:(?:0[1-9])|(?:1[0-2]))(?:(?:[0-2][1-9])|(?:[1-3][0-1]))$";
            if (!Pattern.matches(regx, endStr)) {
                return "证件有效截止日期无效";
            }
            if (!Pattern.matches(regx, beginStr)) {
                return "证件有效起始日期无效";
            }
            if (!Pattern.matches(regx, birthday)) {
                return "出生日期无效";
            }

            Long begin = dateFormat_input.parse(beginStr).getTime();
            Long end = dateFormat_input.parse(endStr).getTime();
            Long birth = dateFormat_input.parse(birthday).getTime();

            Long now = System.currentTimeMillis();
            if (begin < birth) {
                return "开始日期早于出生日期";
            }
            if (begin > now) {
                return "开始日期晚于今天";
            }
            if (end < now) {
                return "您的身份证已到期";
            }
        } catch (ParseException e) {
            logger.warn("日期格式转换错误：begin:" + beginStr + ",end:" + endStr + ",birthday:" + birthday);
            return "日期格式不符合规则。";

        }
        return "1";
    }

    /**
     * 格式化输出显示（填写的时候）  HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String formatDate_noday(Date date) {
        if (date == null) return "";
        return timeSecFormat.format(date);
    }

    /**
     * 获取星期 1星期天 2星期一 3星期二 4星期三 5星期四 6星期五 7星期六
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /*
     * 判断是否在月中
     */
    public static boolean isBettwenMonth(String giveDateStr) {
        Date giveDate = DateUtil.parseDate(giveDateStr);
        Date nowDate = new Date();
        Date beginDate = DateUtil.monthBegin(nowDate);
        Date endDate = DateUtil.monthEnd(nowDate);
        return isBetweenDateS(beginDate, giveDate, endDate);
        // return giveDate.before(endDate) && giveDate.after(beginDate);

    }

    /*
     * 判断是否在年中
     */
    public static boolean isAfterYear(String giveDateStr) {

        Date giveDate = DateUtil.parseDate(giveDateStr);

        Date beginDate = DateUtil.getCurrYearFirst();
        return giveDate.after(beginDate) || giveDate.equals(beginDate);
        // return giveDate.before(endDate) && giveDate.after(beginDate);

    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }


    public static Date parse2SqlDateTimes(String source) {
        if (source == null || source.trim().length() == 0) return null;
        try {
            Date returnDate = timeSecFormat.parse(source);
            return returnDate;
        } catch (ParseException e) {
            logger.error("DateUtil parseDate error", e);
            return null;
        }
    }

    public static boolean isbettwenThreeMonth(String investPoliofoCreateTime) {
        Date giveDate = DateUtil.parseDate(investPoliofoCreateTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -2);
        Date begin = monthBegin(calendar.getTime());
        return giveDate.after(begin);
    }
}
