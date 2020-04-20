package com.hmk.javaweb.utils;

import com.hmk.javaweb.constant.SysRunException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: StringUtils
 * @Description: 字符处理工具类
 * @author zhouhuachang
 * @date 2014-3-10 下午09:25:58
 */
public final class StringUtil {

    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 判断对象或字符串是否为空
     * 
     * @param obj
     * @return
     */
    public static boolean isBlank(Object obj) {
        boolean flag = false;
        if (null == obj || "".equals(obj.toString().trim())) {
            flag = true;
        }
        return flag;
    }

    @SuppressWarnings("unchecked")
	public static <T extends Number> List<T> split(String str, String sp) throws ParseException {
        List<T> list = new ArrayList<T>();
        String[] arr =   str.split(sp);
        for(String e : arr){
            list.add((T) NumberFormat.getInstance().parse(e));
        }
        return list;
    }

    public static <T> String concat(List<T> list, String sp){
        StringBuilder sb = new StringBuilder();
        for(T e:list){
           sb.append(e.toString()+sp);
        }
        return sb.deleteCharAt(sb.length()-1).toString();

    }


    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }

    // ======================获取字符串==========================

    /**
     * 获取随机的32位UUID字符串：(已将其中的"-"替换掉)
     * <p>
     * 另：还可使用RandomGUID <br>
     * 地址：http://www.javaexchange.com/aboutRandomGUID.html
     * 
     * @return
     */
    public static String getRandomUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 随机获取指定长度的字符串(仅由数字组成)：
     * 
     * @param length 获取的长度
     * @return 长度小于或等于0时，则返回""
     */
    public static String getRandomStr4Number(Integer length) {
        if (length <= 0) {
            return "";
        }

        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }

    /**
     * 随机获取指定长度的字符串(由数字与字母组成)：
     * <p>
     * 另：如果导入commons-lang.jar包，可用此类： <br>
     * RandomStringUtils.randomAlphanumeric(length)
     * 
     * @param length 获取的长度
     * @return 长度小于或等于0时，则返回""
     */
    public static String getRandomStr(Integer length) {
        if (length <= 0) {
            return "";
        }

        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(random.nextInt(str.length())));
        }
        return sb.toString();
    }

    /**
     * 得到一个日期加随机数的字符串
     * 
     * @param length 随机数的个数，如不加随机数，则用0代替
     * @return 如果length=8，返回形式为：20110318103647015PVje2TJO
     */
    public static String getDateAndRandStr(Integer length) {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + getRandomStr(length);
    }

    // -------------------------------------------------------------------------
    // 其它：
    // 1、重复输出多个字符
    // 2、对于指定属性的第一个字母大写
    // 3、去掉字符串的最后一个字符
    // 4、截取指定长度字符
    // 5、字符编码
    // 6、将字符串转为输入流
    // -------------------------------------------------------------------------

    /**
     * 重复输出多个字符 <br>
     * (说明：此方法直接拷贝于"org.apache.commons.lang.StringUtils")
     * 
     * <pre>
     * StringUtils.padding(0, 'e')  = ""
     * StringUtils.padding(3, 'e')  = "eee"
     * StringUtils.padding(-2, 'e') = IndexOutOfBoundsException
     * </pre>
     */
    public static String padding(Integer repeat, char padChar) throws IndexOutOfBoundsException {
        if (repeat < 0) {
            throw new IndexOutOfBoundsException("Cannot pad a negative amount: " + repeat);
        }
        final char[] buf = new char[repeat];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = padChar;
        }
        return new String(buf);
    }

    /**
     * 对于指定属性的第一个字母大写
     * 
     * @param propName 属性名
     */
    public static String upperFirstCase(String propName) {
        if (isBlank(propName)) {
            throw new SysRunException("指定属性名称不能为空！");
        }

        return String.valueOf(propName.charAt(0)).toUpperCase() + propName.substring(1);
    }

    /**
     * 去掉字符串的最后一个字符(常用于去掉最后一个分号(;))
     * 
     * @param str 目标字符串
     * @return 如果为null，或长度为0，则返回本身
     */
    public static String removeLastChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }

        return str.substring(0, str.length() - 1);
    }

    /**
     * 截取指定长度字符串(如果要截取的长度小于指定长度则返回原字符)
     * 
     * @param oldstr
     * @param length 要截取的长度
     * @return
     */
    public static String getSubString(String oldstr, Integer length) {
        if (isBlank(oldstr)) {
            throw new SysRunException("要截取的字符不能为空");
        }
        if (oldstr.length() >= length) return oldstr.substring(0, length);
        return oldstr;
    }

    /**
     * 将字符转换为指定编码字符(常用于get方法中文参数转换)
     * 
     * @param oldStr
     * @param charset 指定编码
     * @return
     */
    public static String encodeStr(String oldStr, String charset) {
        if (isBlank(oldStr)) {
            throw new SysRunException("要编码的字符不能为空");
        }

        try {
            return new String(oldStr.getBytes("iso-8859-1"), charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串转换为流
     * 
     * @param input
     * @param encoding
     * @return
     */
    public static InputStream getStreamFromStr(String input, String encoding) {
        if (isBlank(input)) {
            throw new SysRunException("要转换为流的字符不能为空");
        }
        try {
            return IOUtils.toInputStream(input, encoding);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将字符串的指定位置替换成新的字符
     * 
     * @param str 原字符串
     * @param n 指定要替换的位值(从1开始)
     * @param newChar 要替换的字符
     * @return String 替换后的字符串
     * @throws Exception
     */
    public static String replace(String str, int n, String newChar) throws Exception {
        String s1 = "";
        String s2 = "";
        try {
            s1 = str.substring(0, n - 1);
            s2 = str.substring(n, str.length());
        } catch (Exception ex) {
            throw new SysRunException("替换的位数大于字符串的位数");
        }
        return s1 + newChar + s2;
    }

    /**
     * 反转义&midddot中点
     * 
     * @param str
     * @return
     */
    public static String middotUnescape(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("&middot;", "·");
    }

    /**
     * 把中文转成Unicode码
     * 
     * @param str
     * @return
     */
    public static String chinaToUnicode(String str) {
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = str.charAt(i);
            if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                result += "\\u" + Integer.toHexString(chr1);
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }

    /**
     *  对字符串数据排序
     * @param keys
     * @return
     */
    public static String[] sortArrByASCII(String[] keys){

        for (int i = 0; i < keys.length - 1; i++) {
            for (int j = 0; j < keys.length - i -1; j++) {
                String pre = keys[j];
                String next = keys[j + 1];
                if(isMoreThan(pre, next)){
                    String temp = pre;
                    keys[j] = next;
                    keys[j+1] = temp;
                }
            }
        }
        return keys;
    }


    /**
     * 比较两个字符串的大小，按字母的ASCII码比较
     * @param pre
     * @param next
     * @return
     */
    private static boolean isMoreThan(String pre, String next){
        if(null == pre || null == next || "".equals(pre) || "".equals(next)){
            logger.error("字符串比较数据不能为空！");
            return false;
        }

        char[] c_pre = pre.toCharArray();
        char[] c_next = next.toCharArray();

        int minSize = Math.min(c_pre.length, c_next.length);

        for (int i = 0; i < minSize; i++) {
            if((int)c_pre[i] > (int)c_next[i]){
                return true;
            }else if((int)c_pre[i] < (int)c_next[i]){
                return false;
            }
        }
        if(c_pre.length > c_next.length){
            return true;
        }

        return false;
    }

    public static void main(String[] args){
        String[] arr1 = {"VERSION","SERVICE_ID","SIGN_TYPE","CHANNEL_ID"};
        String[] arr2 = {"CUACCT_CODE","TRD_PWD","SESSION"};
//        System.out.println(Arrays.toString(sortArrByASCII(arr1)));
//        System.out.println(Arrays.toString(sortArrByASCII(arr2)));

        System.out.println(" StringUtils.join : "+StringUtils.join(arr1,"-"));
        System.out.println(" String.join :"+String.join("-",arr2));
    }

}
