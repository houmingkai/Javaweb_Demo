/*
 * Copyright (c)  2011-2016. 10jqka.com.cn All right reserved. This software is the confidential and proprietary information
 *  of 10jqka.com.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it
 *  only in accordance with the terms of the license agreement you entered into with 10jqka.com.cn.
 */
package com.hmk.javaweb.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @ClassName: NumberUtil
 * @Description: 数字处理工具类
 * @Author: zhouhuachang@myhexin.com
 * @CreateDate: 2015-1-26 下午5:25:44
 * @version:1.0
 */
public class NumberUtil {
    public static final Long LONG_ZERO = new Long(0L);
    public static final Integer INTEGER_ZERO = new Integer(0);
    public static final Short SHORT_ZERO = new Short((short) 0);
    public static final Byte BYTE_ZERO = new Byte((byte) 0);
    public static final Double DOUBLE_ZERO = new Double(0.0d);
    public static final Float FLOAT_ZERO = new Float(0.0f);
    
    /***
     * 比较，相等就返回true 如Integer a=1;Double b=1.00000;比较也会相等
     * @param a
     * @param b
     * @return
     */
    public static <T extends Number> boolean equals(T a, T b){
        if(a==null || b==null) return false;
        BigDecimal b1 = new BigDecimal(a.toString());
        BigDecimal b2 = new BigDecimal(b.toString());
        if( b1.compareTo(b2)==0)
            return true;
        return false;
    }



    public static String getRandomNumber(int digCount) {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(digCount);
        for(int i=0; i < digCount; i++)
            sb.append((char)('0' + rnd.nextInt(10)));
        return sb.toString();
    }


    /**
     * isZero(null) is true
     * isZero(0) is true
     * isZero(0.0) is true
     * isZero(0.1) is false
     * @param a
     * @return
     */
    public static <T extends Number> boolean isZero(T a){
        if(a==null ) return true;
        BigDecimal b1 = new BigDecimal(a.toString());
        BigDecimal b2 = new BigDecimal(0);
        if( b1.compareTo(b2)==0)
            return true;
        return false;
    }
    
    /***
     * 比较，相等就返回true 
     * 带向下取整
     * 如Integer a=1;Double b=1.00001;int scale=3;比较也会相等
     * @param a
     * @param b
     * @return
     */
    public static <T extends Number> boolean equals(T a, T b, int scale){
        if(a==null || b==null) return false;
        BigDecimal b1 = new BigDecimal(a.toString()).setScale(scale,BigDecimal.ROUND_HALF_DOWN);
        BigDecimal b2 = new BigDecimal(b.toString()).setScale(scale,BigDecimal.ROUND_HALF_DOWN);
        if( b1.compareTo(b2)==0)
            return true;
        return false;
    }
    
    
    /***
     * 乘
     * @param v1
     * @param v2
     * @return
     */
    public static <T extends Number> Double mul(T v1, T v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2).doubleValue();
        
    }

    /**
     * 不同类型乘
     * @param v1
     * @param v2
     * @param <A>
     * @param <B>
     * @return
     */
    public static <A extends Number,B extends Number> BigDecimal mulToBig(A v1, B v2){
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.multiply(b2);

    }


    
    
    /**
     * 两个Double数相除，并保留scale位小数
     * 
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    public static <T extends Number> Double div(T v1, T v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_FLOOR).doubleValue();
    }

    public static <A extends Number,B extends Number> BigDecimal divToBig(A v1, B v2, int scale){
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2, scale, BigDecimal.ROUND_FLOOR);

    }


    /**
     * 保留double scale位小数
     * 
     * @param v
     * @param scale
     * @return
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String round(String str) {
        if (str != null && str.indexOf(".") != -1) {
            int i = str.indexOf(".");
            if (str.length() - i > 2) {
                return str.substring(0, i + 3);
            } else if (str.length() - i > 1) {
                str = str + "0";
            }
        } else if (str == null) {
            return "0.00";
        } else {
            str = str + ".00";
        }
        return str;
    }

    /**
     * 四舍五入取整
     * @param v
     * @return
     */
    public static Long roundLong(double v){
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 0, BigDecimal.ROUND_HALF_UP).longValue();
    }

    /**
     * 判断当前值是否为整数
     * 
     * @param value
     * @return
     */
    public static boolean isInteger(Object value) {
        if (null == value) {
            return false;
        }
        String mstr = value.toString();
        Pattern pattern = Pattern.compile("^-?\\d+{1}");
        return pattern.matcher(mstr).matches();
    }

    /**
     * s 判断当前值是否为数字(包括小数)
     * 
     * @param value
     * @return
     */
    public static boolean isDigit(Object value) {
        if (null == value) {
            return false;
        }
        String mstr = value.toString();
        Pattern pattern = Pattern.compile("^-?[0-9]*.?[0-9]*{1}");
        return pattern.matcher(mstr).matches();
    }

    /**
     * 将数字格式化输出
     * 
     * @param value 需要格式化的值
     * @param precision 精度(小数点后的位数)
     * @return
     */
    public static String format(Object value, Integer precision) {
        Double number = 0.0;
        if (isDigit(value)) {
            number = new Double(value.toString());
        }
        precision = (precision == null || precision < 0) ? 2 : precision;
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(precision, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 将数字格式化输出
     * 
     * @param value
     * @return
     */
    public static String format(Object value) {
        return format(value, 2);
    }

    /**
     * 将值转成Integer型，如果不是整数，则返回0
     * 
     * @param value
     * @param replace 如果为0或者null，替换值
     * @return
     */
    public static Integer parseInteger(Object value, Integer replace) {
        if (!isInteger(value)) {
            return replace;
        }
        return new Integer(value.toString());
    }

    /**
     * 将值转成Integer型，如果不是整数，则返回0
     * 
     * @param value
     * @return
     */
    public static Integer parseInteger(Object value) {
        return parseInteger(value, 0);
    }

    /**
     * 将值转成Long型
     * 
     * @param value
     * @param replace 如果为0或者null，替换值
     * @return
     */
    public static Long parseLong(Object value, Long replace) {
        if (!isInteger(value)) {
            return replace;
        }
        return new Long(value.toString());
    }

    /**
     * 将值转成Long型，如果不是整数，则返回0
     * 
     * @param value
     * @return
     */
    public static Long parseLong(Object value) {
        return parseLong(value, 0L);
    }

    /**
     * 将值转成Double型
     * 
     * @param value
     * @param replace replace 如果为0或者null，替换值
     * @return
     */
    public static Double parseDouble(Object value, Double replace) {
        if (!isDigit(value)) {
            return replace;
        }
        return new Double(value.toString());
    }

    /**
     * 将值转成Double型，如果不是整数，则返回0
     * 
     * @param value
     * @return
     */
    public static Double parseDouble(Object value) {
        return parseDouble(value, 0.0);
    }

    /**
     * 将char型数据转成字节数组
     * 
     * @param value
     * @return
     */
    public static byte[] toBytes(char value) {
        byte[] bt = new byte[2];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将short型数据转成字节数组
     * 
     * @param value
     * @return
     */
    public static byte[] toBytes(short value) {
        byte[] bt = new byte[2];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将int型数据转成字节数组
     * 
     * @param value
     * @return
     */
    public static byte[] toBytes(int value) {
        byte[] bt = new byte[4];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将long型数据转成字节数组
     * 
     * @param value
     * @return
     */
    public static byte[] toBytes(long value) {
        byte[] bt = new byte[8];
        for (int i = 0; i < bt.length; i++) {
            bt[i] = (byte) (value >>> (i * 8));
        }
        return bt;
    }

    /**
     * 将short型数据插入到指定索引的字节数组中
     * 
     * @param index 索引
     * @param values 字节数组
     * @param value 需要插入的值
     */
    public static void insert(int index, byte[] values, short value) {
        byte[] bt = toBytes(value);
        System.arraycopy(bt, 0, values, index, 2);
    }

    /**
     * 将int型数据插入到指定索引的字节数组中
     * 
     * @param index 索引
     * @param values 字节数组
     * @param value 需要插入的值
     */
    public static void insert(int index, byte[] values, int value) {
        byte[] bt = toBytes(value);
        System.arraycopy(bt, 0, values, index, 4);
    }

    /**
     * 将long型数据插入到指定索引的字节数组中
     * 
     * @param index 索引
     * @param values 字节数组
     * @param value 需要插入的值
     */
    public static void insert(int index, byte[] values, long value) {
        byte[] bt = toBytes(value);
        System.arraycopy(bt, 0, values, index, 8);
    }

    /**
     * 将字节转换成整型
     * 
     * @param value 字节类型值
     * @return
     */
    public static int byteToInt(byte value) {
        if (value < 0) {
            return value + 256;
        }
        return value;
    }

    /**
     * 将一个百分比字符转换为小数
     * 
     * @param percent
     * @param scale
     * @return
     */
    public static double convertPercentToScale(String percent, int scale) {
        if (StringUtils.isBlank(percent)) {
            return round(0, scale);
        }

        if (percent.contains("%")) {
            percent = percent.replaceAll("%", "");
            double f = Double.valueOf(percent);
            return div(f, 100.0, scale);
        }

        // 不是百分比的精确返回
        return round(parseDouble(percent), scale);
    }
    
    /**
     * 格式化输出字符串类型数字为指定小数位数
     * @param args
     */
    
    public static String formatStringNumber(Object obj , int scale){
        StringBuffer formatB = new StringBuffer("###0.");
        for(int i=0;i<scale;i++){
            formatB.append("0");
        }
        DecimalFormat decimalFormat = new DecimalFormat(formatB.toString());// 格式化设
        return decimalFormat.format(obj);
    }
    
    /**
     * 根据String 列表求取方差  List内可以包含 -6.6E-4 和 -0.03033 格式
     * @param List<String> 
     * @return BigDecimal
     */
  	public static BigDecimal getVarianceFromStrList(List<String> numList) {
  		ArrayList<BigDecimal> bigList = new ArrayList<>();
  		BigDecimal sum = new BigDecimal(0);
  		for (String everNum : numList) {
  			BigDecimal bigEver = new BigDecimal(everNum);
  			sum = sum.add(bigEver);
  			bigList.add(bigEver);
  		}
  		BigDecimal count = new BigDecimal(bigList.size());
  		BigDecimal avg = sum.divide(count, 10, RoundingMode.HALF_UP);
  		BigDecimal sum2 = new BigDecimal(0);
  		for (BigDecimal bigDecimal : bigList) {
  			BigDecimal subtract = bigDecimal.subtract(avg);
  			double pow = Math.pow(subtract.doubleValue(), 2);
  			BigDecimal bigDecimal2 = new BigDecimal(pow);
  			sum2 = sum2.add(bigDecimal2);
  		}
  		BigDecimal divide = sum2.divide(count, 10, RoundingMode.HALF_UP);
  		System.out.println(divide.doubleValue());
  		return divide;
  	}
  	/**
     * 根据Double 列表求取方差  
     * @param List<Double> 
     * @return BigDecimal
     */
  	public static BigDecimal getVarianceFromDoubleList(List<Double> numList) {
  		ArrayList<BigDecimal> bigList = new ArrayList<>();
  		BigDecimal sum = new BigDecimal(0);
  		for (Double everNum : numList) {
  			BigDecimal bigEver = new BigDecimal(everNum);
  			sum = sum.add(bigEver);
  			bigList.add(bigEver);
  		}
  		BigDecimal count = new BigDecimal(bigList.size());
  		BigDecimal avg = sum.divide(count, 10, RoundingMode.HALF_UP);
  		BigDecimal sum2 = new BigDecimal(0);
  		for (BigDecimal bigDecimal : bigList) {
  			BigDecimal subtract = bigDecimal.subtract(avg);
  			double pow = Math.pow(subtract.doubleValue(), 2);
  			BigDecimal bigDecimal2 = new BigDecimal(pow);
  			sum2 = sum2.add(bigDecimal2);
  		}
  		BigDecimal divide = sum2.divide(count, 10, RoundingMode.HALF_UP);
  		System.out.println(divide.doubleValue());
  		return divide;
  	}
  	//对传入的参数求2次方根
  	public static BigDecimal rooting(BigDecimal a) {
		BigDecimal _2 = BigDecimal.valueOf(2.0);
		//设置精度以及近似取值方式(精度100,四舍五入)
		int precision=100;
		MathContext mc = new MathContext(precision, RoundingMode.HALF_UP);
		if(a.compareTo(BigDecimal.ZERO)==0) {
			System.out.println("负数不能求开平方，此方法不适用于复数");
			return null;
		}else {
			BigDecimal x=a;
			int cnt=0;
			while(cnt<100) {
				x=(x.add(a.divide(x,mc))).divide(_2,mc);
				cnt++;
			}
			return x;
		}
	}


  	/**
  	 * string列表获取平均值
  	 * 
  	 */
	public static BigDecimal getAvgFromSList(List<String> a ) {
		BigDecimal sum = new BigDecimal(0);
		for (String eveNum : a) {
			BigDecimal number = new BigDecimal(eveNum);
			sum=sum.add(number);
		}
		BigDecimal dividor = new BigDecimal(a.size());
		BigDecimal divide = sum.divide(dividor, 20, RoundingMode.HALF_UP);
		return divide;
	}
	/**
  	 * double列表获取平均值
  	 * 
  	 */
	public static BigDecimal getAvgFromDList(List<Double> a ) {
		BigDecimal sum = new BigDecimal(0);
		for (double eveNum : a) {
			BigDecimal number = new BigDecimal(eveNum);
			sum=sum.add(number);
		}
		BigDecimal dividor = new BigDecimal(a.size());
		BigDecimal divide = sum.divide(dividor, 20, RoundingMode.HALF_UP);
		return divide;
	}





	public static void main(String[] args){

	}
}
