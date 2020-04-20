/*
 * Copyright 2011-2015 10jqka.com.cn All right reserved. This software is the confidential and proprietary information
 * of 10jqka.com.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered into with 10jqka.com.cn.
 */
package com.hmk.javaweb.controller;

import org.apache.commons.io.IOUtils;
//import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 类HuaweiHttpUtil.java的实现描述：华为HTTPS请求
 * 
 * @author Huangxiquan 2017年8月1日 上午10:15:14
 */
public class HuaweiHttpUtil {

    /**
     * 日志工具
     */
    private static Logger logger = LoggerFactory.getLogger(HuaweiHttpUtil.class);

    /**
     * 华为http请求
     * 
     * @param httpUrl 请求连接
     * @param data 请求参数
     * @param connectTimeOut 连接超时时间
     * @param readTimeOut 读取超时时间
     * @return
     */
    public static String httpPost(String httpUrl, String data, int connectTimeOut, int readTimeOut) {
        // 输出流
        OutputStream out = null;
        // http的url连接
        HttpURLConnection conn = null;
        // 输入流
        InputStream in = null;

        try {
            URL url = new URL(httpUrl);
            // 获取url连接
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// post 请求
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setConnectTimeout(connectTimeOut);
            conn.setReadTimeout(readTimeOut);
            conn.connect();// 进行url连接

            // 获取输出流,将参数写入到输出流
            out = conn.getOutputStream();
            out.write(data.getBytes("UTF-8"));
            out.flush();

            // 获取返回信息输入流
            in = conn.getInputStream();
//            if (conn.getResponseCode() < HttpResponseStatus.BAD_REQUEST.getCode()) {
//                in = conn.getInputStream();
//            } else {
//                in = conn.getErrorStream();
//            }

            // 获取返回信息
            List<String> lines = IOUtils.readLines(in, conn.getContentEncoding());
            // 拼接返回信息
            StringBuffer response = new StringBuffer();
            for (String line : lines) {
                response.append(line);
            }
            logger.debug("httpPost::url->{},response->{}", httpUrl, response.toString());

            return response.toString();
        } catch (Exception e) {
            logger.error("httpPost::华为HTTPS请求异常!", e);
        } finally {
            if (out != null) {
                // 关闭输出流
                IOUtils.closeQuietly(out);
            }
            if (in != null) {
                // 关闭输入流
                IOUtils.closeQuietly(in);
            }
            if (conn != null) {
                // 关闭url连接
                conn.disconnect();
            }
        }

        return null;
    }
}
