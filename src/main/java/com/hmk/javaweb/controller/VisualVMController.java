package com.hmk.javaweb.controller;


import com.hmk.javaweb.constant.ReturnMessage;
import com.hmk.javaweb.visualVM.JavaHeapTest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.text.MessageFormat;

@RestController
@RequestMapping("/visualvm")
public class VisualVMController {

    private static final Logger logger = LoggerFactory.getLogger(VisualVMController.class);

    @RequestMapping("/javaHeapTest")
    public ReturnMessage javaHeapTest() {
        ReturnMessage returnMessage = new ReturnMessage();
        try {

            JavaHeapTest javaHeapTest = new JavaHeapTest(JavaHeapTest.OUTOFMEMORY);
            returnMessage.setFlag(true);
            returnMessage.setData(javaHeapTest.getOom().length());
        } catch (Exception e) {
            returnMessage.setFlag(false);
            e.printStackTrace();
        }

        return returnMessage;
    }



    public static void main(String[] args){
        String tokenUrl = "https://login.vmall.com/oauth2/token";
        String TOKEN_PATTERN   = "grant_type=client_credentials&client_secret={0}&client_id={1}";
        String appId= "10592551";
        String appSecret = "3c8b0b3ad424b1217c02c006c1dbccd2";
        String msgBody = MessageFormat.format(TOKEN_PATTERN, encode(appSecret), appId);

        String response = HuaweiHttpUtil.httpPost(tokenUrl, msgBody, 5000,
                5000);
        System.out.println(response);
    }

    private static String encode(String param) {
        if (StringUtils.isBlank(param)) {
            return null;
        }

        try {
            // 参数编码
            return URLEncoder.encode(param, "UTF-8");
        } catch (Exception e) {
        }

        return null;
    }
}
