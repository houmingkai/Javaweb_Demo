package com.hmk.javaweb.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 *  AOP切面类
 */
@Component
@Aspect
@Slf4j
public class WebControllerAop {

    private ThreadLocal<Long> startTime       = new ThreadLocal<Long>();

    //指定切点
    @Pointcut("execution(* com.hmk.javaweb.controller..*.*(..))")
    public void pointcut() {
    }

    //前置通知 --打印请求信息(url.uri,方法名,请求参数)
    @Before(value = "pointcut()")
    public void params() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String ipAddr = getRemoteHost(request);
        Map<String, String[]> params = request.getParameterMap();
        String queryString = "";
        if (params != null) {
            for (String key : params.keySet()) {
                String[] values = params.get(key);
                for (int i = 0; i < values.length; i++) {
                    String value = values[i];
                    queryString += key + "=" + value + "&";
                }
            }
            StringUtils.substringBefore(queryString, "&");
        }
        log.info("请求ip :"+ipAddr);
        log.info("请求开始, 各个参数, url: {" + url + "}, method: {" + method + "}, uri: {" + uri + "}, params: {"
                + queryString + "}");
    }

    //后置返回通知---打印方法返回结果
    @AfterReturning(returning = "resultMap", value = "pointcut()")
    public void controllerReturn(JoinPoint jp, Object resultMap) {
        try {
            String result = JSON.toJSONString(resultMap);
            if (result.length() > 1000) {
                result = result.substring(0, 1000) + "..................";
            }
            log.info(jp.getSignature().getName() + "方法 " + "返回结果:" + result);
        } catch (Exception e) {
            log.info(jp.getSignature().getName() + "方法 " + "返回结果:" + JSON.toJSONString(resultMap));
        }
    }

    // 环绕通知----记录时间
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.nanoTime();
        startTime.set(begin);
        Object o = pjp.proceed();
        long end = System.nanoTime();
        log.debug(pjp.getSignature() + " take " + (end - startTime.get()) / 1000000 + " ms");
        return o;
    }


    /**
     * 获取目标主机的ip
     * @param request
     * @return
     */
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

//    public final static String getIpAddress(HttpServletRequest request) throws IOException {
//        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
//
//        String ip = request.getHeader("X-Forwarded-For");
//        if (log.isInfoEnabled()) {
//            log.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
//        }
//
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("Proxy-Client-IP");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
//                }
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("WL-Proxy-Client-IP");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
//                }
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("HTTP_CLIENT_IP");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
//                }
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
//                }
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getRemoteAddr();
//                if (log.isInfoEnabled()) {
//                    log.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
//                }
//            }
//        } else if (ip.length() > 15) {
//            String[] ips = ip.split(",");
//            for (int index = 0; index < ips.length; index++) {
//                String strIp = (String) ips[index];
//                if (!("unknown".equalsIgnoreCase(strIp))) {
//                    ip = strIp;
//                    break;
//                }
//            }
//        }
//        return ip;
//    }

}
