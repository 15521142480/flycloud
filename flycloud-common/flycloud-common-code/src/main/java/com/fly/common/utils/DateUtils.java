package com.fly.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * 时间工具类
 *
 * @author lxs
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final String YYYY = "yyyy";

    public static final String YYYY_MM = "yyyy-MM";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static final String[] PARSE_PATTERNS = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期
     *
     * @param pattern
     */
    public static final String getCurDateTime(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期日期转换
     */
    public static String format(long date) {
        return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(new Date(date));
    }

    public static String format(long date, String format) {
        return new SimpleDateFormat(format).format(new Date(date));
    }

    /**
     * 日期日期转换
     */
    public static Date formatDate(long date) {
        return new Date(date);
    }


    /**
     * 格式化日期
     *
     * @param date
     * @return
     */
    public static final String format(Object date) {
        return format(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static final String format(Object date, String pattern) {

        if (date == null) {
            return null;
        }
        if (pattern == null) {
            return format(date, YYYY_MM_DD_HH_MM_SS);
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式化日期
     * String -> Date
     * @param dateStr
     * @param pattern
     * @return
     */
    public static final Date format(String dateStr, String pattern) throws ParseException {
        if (dateStr == null) {
            return null;
        }
        if (pattern == null) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return new SimpleDateFormat(pattern).parse(dateStr);
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), PARSE_PATTERNS);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算相差天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    /**
     * 增加 LocalDateTime ==> Date
     */
    public static Date toDate(LocalDateTime temporalAccessor) {
        ZonedDateTime zdt = temporalAccessor.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }

    /**
     * 增加 LocalDate ==> Date
     */
    public static Date toDate(LocalDate temporalAccessor) {
        LocalDateTime localDateTime = LocalDateTime.of(temporalAccessor, LocalTime.of(0, 0, 0));
        ZonedDateTime zdt = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }


    /**
     * 获取距离当前时间前或者后几个月的年月
     *
     * @return
     */
    public static String getYearMonth(int num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, num);
        return sdf.format(cal.getTime());
    }

    /**
     *  获取开始与结束时间之间的日期列表
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getBetweenDateList(String startDate, String endDate, String dateFormat) throws ParseException {
        Date startTime = format(startDate, dateFormat);
        Date endTime = format(endDate, dateFormat);
        List<String> dateList = new ArrayList<>();
        while(startTime.compareTo(endTime) != 1) {
            dateList.add(format(startTime, dateFormat));
            startTime.setTime(startTime.getTime() + 1000 * 60 * 60 * 24); // 加一天
        }
        return dateList;
    }

    /**
     * 获取当天使第几周
     * @param dt
     * @return
     */
    public static int getCurWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int week = cal.get(7) - 1;
        if (week < 0) {
            week = 0;
        }

        return week;
    }

    /**
     * 单独获取当前年月日数字(去零处理)
     * 参数(yyyy  MM  dd)
     * @return
     */
    public static int getCurDateNum(String format) {

        String num = "";

        if (format.equals("yyyy")) {
            num = getCurDateTime(format);
        } else {

            num = getCurDateTime(format);
            if (num.contains("0")) {
                num = num.replace("0", "");
            }
        }

        return Integer.parseInt(num);
    }




    ////////////////////////////////////////////////////////////////////////////// 計算時間的交集或並集 start

    /**
     * 获取两个时间段的交并集
     *
     * @param periods1 第一个时间段
     * @param periods2 第二个时间段
     * @param type     0 交集 1 并集
     * @return 不存在为null
     */
    public static List<LocalDate[]> twoTimePeriods(LocalDate[] periods1, LocalDate[] periods2, int type) {

        //校验时间段1是否有效
        boolean valid1 = Objects.nonNull(periods1)
                && periods1.length > 1
                && periods1[0] != null
                && periods1[1] != null
                && (periods1[0].isEqual(periods1[1]) || periods1[0].isBefore(periods1[1]));
        //校验时间段2是否有效
        boolean valid2 = Objects.nonNull(periods2)
                && periods2.length > 1
                && periods2[0] != null
                && periods2[1] != null
                && (periods2[0].isEqual(periods2[1]) || periods2[0].isBefore(periods2[1]));
        if (valid1 && valid2) {
            if (0 == type) {
                LocalDate[] intersection = twoTimePeriodsIntersection(periods1, periods2);
                if (Objects.nonNull(intersection)) {
                    return Collections.singletonList(intersection);
                }
            }
            if (1 == type) {
                return twoTimePeriodsUnion(periods1, periods2);
            }
        }

        return null;
    }

    /**
     * 获取两个时间段的交集
     *
     * @param periods1 第一个时间段
     * @param periods2 第二个时间段
     * @return 不存在为null
     */
    public static LocalDate[] twoTimePeriodsIntersection(LocalDate[] periods1, LocalDate[] periods2) {

        //判断两个时间段是否存在交集
        if (!(periods1[0].isAfter(periods2[1]) || periods2[0].isAfter(periods1[1]))) {
            return new LocalDate[]{
                    periods1[0].isAfter(periods2[0]) ? periods1[0] : periods2[0],
                    periods1[1].isBefore(periods2[1]) ? periods1[1] : periods2[1]
            };
        }

        return null;
    }

    /**
     * 获取两个时间段的并集
     *
     * @param periods1 第一个时间段
     * @param periods2 第二个时间段
     * @return 存在交集 一个 LocalDate[] 不存在 两个 LocalDate[]
     */
    public static List<LocalDate[]> twoTimePeriodsUnion(LocalDate[] periods1, LocalDate[] periods2) {

        List<LocalDate[]> periodsList = new ArrayList<>(2);
        //判断两个时间段是否存在交集
        if (periods1[0].isAfter(periods2[1]) || periods2[0].isAfter(periods1[1])) {
            //不存在
            periodsList.add(periods1);
            periodsList.add(periods2);
        } else {
            //存在
            periodsList.add(
                    new LocalDate[]{
                            periods1[0].isBefore(periods2[0]) ? periods1[0] : periods2[0],
                            periods1[1].isAfter(periods2[1]) ? periods1[1] : periods2[1]
                    }
            );
        }

        return periodsList;
    }


    ////////////////////////////////////////////////////////////////////////////// 計算時間的交集或並集 end


    public static void main(String[] args) {
        List<LocalDate[]> localDates = twoTimePeriods(
                new LocalDate[]{LocalDate.of(2023, 8, 12), LocalDate.of(2023, 8, 17)},
                new LocalDate[]{LocalDate.of(2023, 8, 13), LocalDate.of(2023, 8, 20)},
                0
        );
        if (localDates != null && !localDates.isEmpty()) {
            for (LocalDate[] periods : localDates) {
                System.out.println(Arrays.toString(periods));
            }
        }
    }

}
