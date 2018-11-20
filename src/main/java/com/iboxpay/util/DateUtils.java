package com.iboxpay.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final String DATE_TIME_MILLS_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

  private static DateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);

  /**
   * 将字符串按格式转换为日期
   * 
   * @param s
   *            日期字符串
   * @param format
   *            转换格式
   * @return 转换后的日期
   * @throws ParseException
   */
  public static Date parseStringToDate(String s, String format) throws ParseException {
    if (format != null) {
      df = new SimpleDateFormat(format);
    }
    Date d = df.parse(s);
    return d;
  }

  /**
   * 将日期按格式转换为字符串
   * 
   * @param date
   *            日期
   * @param format
   *            转换格式
   * @return 转换后的字符串
   */
  public static String parseDateToString(Date date, String format) {
    if (format != null) {
      df = new SimpleDateFormat(format);
    }
    String s = df.format(date);
    return s;
  }

}
