package com.lessly.center.server.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

/**
 * 通用的AES加密解密工具
 */
public class EncryptUtil {

    private static final String AES_ALG         = "AES";

    private static final String charset         = "utf-8";

    //AES算法
    private static final String AES_CBC_PCK_ALG = "AES/CBC/PKCS5Padding";

    private static final byte[] AES_IV          = initIv(AES_CBC_PCK_ALG);

    /**
     * 加密
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String encryptContent(String content, String encryptType, String encryptKey,String charset) throws Exception {
        if (AES_ALG.equals(encryptType)) {
            return aesEncrypt(content, encryptKey);
        } else {
            throw new Exception("当前不支持该算法类型：encrypeType=" + encryptType);
        }

    }

    /**
     * 解密
     * @param content
     * @param encryptType
     * @param encryptKey
     * @param charset
     * @return
     * @throws Exception
     */
    public static String decryptContent(String content, String encryptType, String encryptKey,String charset) throws Exception {
        if (AES_ALG.equals(encryptType)) {
            return aesDecrypt(content, encryptKey);
        } else {
            throw new Exception("当前不支持该算法类型：encrypeType=" + encryptType);
        }

    }

    /**
     * AES加密
     * @param content
     * @param aesKey
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String content, String aesKey)throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);

            IvParameterSpec iv = new IvParameterSpec(AES_IV);
            cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(aesKey.getBytes(), AES_ALG), iv);

            byte[] encryptBytes = cipher.doFinal(content.getBytes(charset));
            return new String(Base64.encodeBase64(encryptBytes));
        } catch (Exception e) {
            throw new Exception("AES加密失败：Aescontent = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * AES解密
     * @param content 待解密串
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String content, String key)throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PCK_ALG);
            IvParameterSpec iv = new IvParameterSpec(initIv(AES_CBC_PCK_ALG));
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(),
                    AES_ALG), iv);

            byte[] cleanBytes = cipher.doFinal(Base64.decodeBase64(content.getBytes()));
            return new String(cleanBytes, charset);
        } catch (Exception e) {
            throw new Exception("AES解密失败：Aescontent = " + content + "; charset = " + charset, e);
        }
    }

    /**
     * 初始向量的方法, 全部为0. 这里的写法适合于其它算法,针对AES算法的话,IV值一定是128位的(16字节).
     * @param fullAlg
     * @return
     * @throws GeneralSecurityException
     */
    private static byte[] initIv(String fullAlg) {
        try {
            Cipher cipher = Cipher.getInstance(fullAlg);
            int blockSize = cipher.getBlockSize();
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        } catch (Exception e) {

            int blockSize = 16;
            byte[] iv = new byte[blockSize];
            for (int i = 0; i < blockSize; ++i) {
                iv[i] = 0;
            }
            return iv;
        }
    }

//    public static void main(String[] args) throws Exception{
//        ObjectMapper objectMapper=new ObjectMapper();
//
//        Map<String,Object> dataMap= Maps.newHashMap();
//        dataMap.put("clientId","vxuwzav2cu");
//        dataMap.put("clientSecret","i5fdanys1wzii6yzfmeiknm2p");
//        dataMap.put("sysCode","system_crm");
//
//        String jsonStr=objectMapper.writeValueAsString(dataMap);
//        System.out.println("明文： "+jsonStr);
//
//        String key="e2bd6cee47e0402db80862a09ff4d126";
//        String encryptStr=aesEncrypt(jsonStr,key);
//        System.out.println("密文： "+ encryptStr);
//
//        String srcInfo=aesDecrypt(encryptStr,key);
//        System.out.println("明文： "+ srcInfo);
//
//        //System.out.println(RandomStringUtils.randomAlphanumeric(10).toLowerCase());
//        //System.out.println(RandomStringUtils.randomAlphanumeric(30).toLowerCase());
//    }

}
