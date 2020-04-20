/*
 * Copyright 2011-2015 10jqka.com.cn All right reserved. This software is the confidential and proprietary information
 * of 10jqka.com.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it
 * only in accordance with the terms of the license agreement you entered into with 10jqka.com.cn.
 */
package com.hmk.javaweb.utils.encode;

import com.hmk.javaweb.constant.SysRunException;
import com.hmk.javaweb.utils.HexConvertUtils;
import com.hmk.javaweb.utils.JsonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RsaUtil {

    /**
     * 默认编码格式
     */
    public static final String DEFAULT_CHARSET                    = "UTF-8";

    /**
     * RSA算法
     */
    public static final String ALGORITHM_RSA                      = "RSA";

    /**
     * 签名算法
     */
    public static final String SIGN_ALGORITHMS                    = "SHA1WithRSA";

    /**
     * RSA公钥map键值
     */
    public static final String RSA_KEY_PUBLIC                     = "RSA_KEY_PUBLIC";

    /**
     * RSA私钥map键值
     */
    public static final String RSA_KEY_PRIVATE                    = "RSA_KEY_PRIVATE";

    /**
     * 日志工具
     */
    private static final Logger logger = LoggerFactory.getLogger(RsaUtil.class);

    /**
     * 私有构造器，防止被创建对象
     */
    private RsaUtil(){
    }

    /**
     * 生成签名
     *
     * @param privateKey
     * @param inputstr
     * @return
     * @throws SysRunException
     */
    public static String sign(String privateKey, String inputstr) throws SysRunException {
        // 加载私钥
        RSAPrivateKey key = loadPrivateKey(privateKey);
        if (key == null) {
            throw new SysRunException("加密私钥为空, 请设置");
        }

        try {
            // 初始化签名算法
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(key);
            signature.update(inputstr.getBytes(DEFAULT_CHARSET));
            // 生成签名
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            throw new SysRunException("生成签名异常！");
        }
    }

    /**
     * 验证签名是否正确
     *
     * @param sign
     * @param publickey
     * @param inputstr
     * @return
     * @throws SysRunException
     */
    public static boolean authenticate(String sign, String publickey, String inputstr) throws SysRunException {
        // 加载公钥
        RSAPublicKey key = loadPublicKey(publickey);
        if (key == null) {
            throw new SysRunException("加密公钥为空, 请设置");
        }

        try {
            // 初始化签名算法
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(key);
            signature.update(inputstr.getBytes(DEFAULT_CHARSET));
            // 校验签名
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            throw new SysRunException("验签异常！");
        }
    }

    /**
     * 根据私钥对字符串进行RSA编码
     *
     * @param privateKey
     * @param inputstr
     * @return
     * @throws SysRunException
     */
    public static String encodeByPrivateKey(String privateKey, String inputstr) throws SysRunException {
        // 加载私钥
        RSAPrivateKey key = loadPrivateKey(privateKey);
        if (key == null) {
            throw new SysRunException("加密私钥为空, 请设置");
        }

        Cipher cipher = null;
        try {
            // 使用默认的RSA
            cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] output = cipher.doFinal(inputstr.getBytes(DEFAULT_CHARSET));
            return Base64.encodeBase64String(output);
        } catch (NoSuchAlgorithmException e) {
            throw new SysRunException("无此加密算法");
        } catch (NoSuchPaddingException e) {
            throw new SysRunException("填充机制不可用,请检查");
        } catch (InvalidKeyException e) {
            throw new SysRunException("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new SysRunException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new SysRunException("明文数据已损坏");
        } catch (UnsupportedEncodingException e) {
            throw new SysRunException("不支持的编码格式");
        } catch (Exception e) {
            throw new SysRunException("RSA私钥加密异常");
        }
    }

    /**
     * 加载私钥
     *
     * @param privateKey
     * @return
     * @throws SysRunException
     */
    private static RSAPrivateKey loadPrivateKey(String privateKey) throws SysRunException {
        try {
            // 将私钥转换成byte数组
            byte[] buffer = Base64.decodeBase64(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            // 初始化秘钥算法
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM_RSA);
            // 生成私钥
            return (RSAPrivateKey) factory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new SysRunException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new SysRunException("私钥非法");
        } catch (NullPointerException e) {
            throw new SysRunException("私钥数据为空");
        } catch (Exception e) {
            throw new SysRunException("RSA加载私钥异常");
        }
    }

    /**
     * 私钥解密
     *
     * @param privateKey
     * @param sign
     * @return
     */
    public static String decodeByPrivateKey(String privateKey, String sign) throws SysRunException {
        // 加载私钥
        RSAPrivateKey key = loadPrivateKey(privateKey);
        if (key == null) {
            throw new SysRunException("加密私钥为空, 请设置");
        }

        Cipher cipher = null;
        try {
            // 使用默认的RSA
            cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, key);
            // 将签名转换成byte数组
            byte[] buffer = Base64.decodeBase64(sign);
            // 解密
            byte[] output = cipher.doFinal(buffer);
            return new String(output, DEFAULT_CHARSET);
        } catch (NoSuchAlgorithmException e) {
            throw new SysRunException("无此加密算法");
        } catch (NoSuchPaddingException e) {
            throw new SysRunException("填充机制不可用,请检查");
        } catch (InvalidKeyException e) {
            throw new SysRunException("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new SysRunException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new SysRunException("明文数据已损坏");
        } catch (UnsupportedEncodingException e) {
            throw new SysRunException("不支持的编码格式");
        } catch (Exception e) {
            throw new SysRunException("RSA私钥加密异常");
        }
    }

    /**
     * 根据公钥对字符串进行RSA编码
     *
     * @param publicKey
     * @param inputstr
     * @return
     * @throws SysRunException
     */
    public static String encodeByPublicKey(String publicKey, String inputstr) throws SysRunException {
        // 加载公钥
        RSAPublicKey key = loadPublicKey(publicKey);
        if (key == null) {
            throw new SysRunException("加密公钥为空, 请设置");
        }

        Cipher cipher = null;
        try {
            // 使用默认的RSA
            cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] output = cipher.doFinal(inputstr.getBytes(DEFAULT_CHARSET));
            return Base64.encodeBase64String(output);
        } catch (NoSuchAlgorithmException e) {
            throw new SysRunException("无此加密算法");
        } catch (NoSuchPaddingException e) {
            throw new SysRunException("填充机制不可用,请检查");
        } catch (InvalidKeyException e) {
            throw new SysRunException("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException e) {
            throw new SysRunException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new SysRunException("明文数据已损坏");
        } catch (UnsupportedEncodingException e) {
            throw new SysRunException("不支持的编码格式");
        } catch (Exception e) {
            throw new SysRunException("RSA公钥加密异常");
        }
    }

    /**
     * 加载公钥
     *
     * @param publicKey
     * @return
     * @throws SysRunException
     */
    private static RSAPublicKey loadPublicKey(String publicKey) throws SysRunException {
        try {
            // 将公钥转换成byte数组
            byte[] buffer = Base64.decodeBase64(publicKey);
            // 初始化公钥算法
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM_RSA);

            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            // 生成公钥
            return (RSAPublicKey) factory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new SysRunException("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new SysRunException("公钥非法");
        } catch (NullPointerException e) {
            throw new SysRunException("公钥数据为空");
        } catch (Exception e) {
            throw new SysRunException("RSA加载公钥异常");
        }
    }

    /**
     * 生成RSA公私钥
     *
     * @return
     */
    public static Map<String, String> generateKeys() throws SysRunException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            // 初始化密钥生成器
            keyPairGen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {
            throw new SysRunException("无此算法");
        }

        try {
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());

            // 生成一个密钥对，保存在keyPair中
            KeyPair keypair = keyPairGen.generateKeyPair();

            // 得到私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keypair.getPrivate();

            // 得到公钥
            RSAPublicKey publicKey = (RSAPublicKey) keypair.getPublic();

            // 定义一个map存放公私钥
            Map<String, String> result = new HashMap<>(3);
            result.put(RSA_KEY_PRIVATE, Base64.encodeBase64String(privateKey.getEncoded()));
            result.put(RSA_KEY_PUBLIC, Base64.encodeBase64String(publicKey.getEncoded()));

            return result;
        } catch (Exception e) {
            throw new SysRunException("生成RSA公私钥异常！");
        }
    }

    public static void main(String[] args) throws IOException {
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKwnVegZzi1STYVVAXmINZH8AuMu4n9nM61uXUiFDsMZmBz6suo08FP+Hy5Od3SuY1E2ePZHa04skhdtxrKwE6lWlkilwQJPOQqMcU2X25o28tQ3dwHnBX8oT5xpXr3DsfbrZVgjufBiDzuUh5Skx74lfXcMyzwm/yQfCQSwBqMFAgMBAAECgYEAnaX77mtLOoenA9V/XzrTy9bbEg5KTl9EnKp3dESc6x8SBF+z3fQSrOgGx9IFZG2Z97IJfYaThmgcdKYLknDZ3u6BwbnIVf/h4liTg3Wbf2ytfUr7sfrV7bg3hbT+JnBZ+e0uGwQKQ+NoUdarCgvJ6GARCuCrGD/tF8w8pQDmfEECQQD88z+jZGNZ0xasr5OLz1VNlTeOuF4KuxLTWGXr17Lp3zBu2fOg3sjU2IjQ5EH8yS4e89L6vCcrbJw8xyLMZn0xAkEArjqzrp+gO6p9x2jHwZVh69Kxu8Tx3hdYMYE0kZFidtNVO/EdEfyx+duMxlsOaWAlYrlaFKcLWLxj60xtCPS+FQJACN6WqqoIecvXDbMSX2KjwRasXgrE8Wdh35tH558mnODAmAJ4cnmdXSlCkuCOF8kOHvO5wCT80p76U39ADx7A4QJAeSnJmz7XJ6dBIVP01uMT3fQANhyYN2L0qji4XiNE3m3JcI4VMVUSUDgmhir8srtyRF2yD3q7oVIiCphzmy7YoQJAO29B0PCQYDE1Jn6eft+sldx7dOAeJiIrDUbaH66S2fe1rWvd6D5hFMD9SffUxVYoENHXEfO0p0zUNCv40oPyfQ==";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsJ1XoGc4tUk2FVQF5iDWR/ALjLuJ/ZzOtbl1IhQ7DGZgc+rLqNPBT/h8uTnd0rmNRNnj2R2tOLJIXbcaysBOpVpZIpcECTzkKjHFNl9uaNvLUN3cB5wV/KE+caV69w7H262VYI7nwYg87lIeUpMe+JX13DMs8Jv8kHwkEsAajBQIDAQAB";
//
//        String pwd = "mZDSCakYZE33XK3lLu5NwxHvXNvfs7mc12a+DdpNhp4tiEotA6pZkqS0ldMIXi3xuBWRGlWroR/LH62M31SX0fBXehpSqZvbGWSZaba+ckt8Z5DDX6WfHSwGwe4mpk2suJWGftXADXME0FR5UWN9YsUsJJtYTtnIOQg9O9IasrY=";
        String pwd = "123456";
        String s = RsaUtil.encodeByPublicKeyNoLimit(publicKey, pwd);
        System.out.println(s);
        System.out.println(RsaUtil.decodeByPrivateKey(privateKey, s));

//        System.out.println(generateKeys());
    }

    /**
     * 私钥解密,返回对象
     *
     * @param privateKey
     *            privateKey
     * @param sign
     *            sign
     * @return map
     */
    public static Map<String, String> decodeByPrivateKeyReturnMap(String privateKey, String sign)
            throws SysRunException, IOException {
        String userParams = RsaUtil.decodeByPrivateKey(privateKey, sign);
        return JsonUtils.toJavaBeanMap(userParams, new TypeReference<Map<String, String>>() {});
    }

    /**
     * 公钥加密(不限长度)
     *
     * @param publicKey 公钥
     * @param data  需要加密字符串
     * @return
     */
    public static String encodeByPublicKeyNoLimit(String publicKey, String data){
        RSAPublicKey key = loadPublicKey(publicKey);
        if (key == null) {
            throw new SysRunException("加密公钥为空, 请设置");
        }
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(DEFAULT_CHARSET), key.getModulus().bitLength()));
        }catch(Exception e){
            throw new SysRunException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    /**
     * 公钥加密为16进制字符串
     *
     * @param publicKey 公钥
     * @return
     */
    public static String publicEncyptHex(String publicKey, byte[] bytes){
        RSAPublicKey key = loadPublicKey(publicKey);
        if (key == null) {
            throw new SysRunException("加密公钥为空, 请设置");
        }
        try{
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return HexConvertUtils.bytesToHexString(cipher.doFinal(bytes));
        }catch(Exception e){
            throw new SysRunException("加密异常", e);
        }
    }
    /**
     * 私钥解密(不限长度)
     *
     * @param privateKey 私钥
     * @param data  需解密字符串
     * @return
     */
    public static String decodeByPrivateKeyNoLimit(String privateKey, String data){
        RSAPrivateKey key = loadPrivateKey(privateKey);
        if (key == null) {
            throw new SysRunException("加密私钥为空, 请设置");
        }
        try{
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data), key.getModulus().bitLength()), DEFAULT_CHARSET);
        }catch(Exception e){
            throw new SysRunException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (IllegalBlockSizeException e) {
            throw new SysRunException("明文长度非法");
        } catch (BadPaddingException e) {
            throw new SysRunException("明文数据已损坏");
        } catch (Exception e) {
            throw new SysRunException("RSA公钥加密异常");
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

}
