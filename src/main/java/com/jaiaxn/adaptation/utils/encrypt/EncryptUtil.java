package com.jaiaxn.adaptation.utils.encrypt;

import org.jasypt.util.text.BasicTextEncryptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: wang.jiaxin
 * @date: 2019年07月08日
 * @description:
 **/
public class EncryptUtil {

    private static String enKey="www.jaiaxn.com";

    /**
     * 加密
     * @param pwd 明文
     * @return 密文
     */
    public static String getEncrypt(String pwd){
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(enKey);
        return textEncryptor.encrypt(pwd);
    }

    /**
     * 解密
     * @param enPwd 密文
     * @return 明文
     */
    public static String getDecrypt(String enPwd){
        BasicTextEncryptor textEncryptor2 = new BasicTextEncryptor();
        textEncryptor2.setPassword(enKey);
        return textEncryptor2.decrypt(enPwd);
    }

}
