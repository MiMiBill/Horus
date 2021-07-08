package com.example.horus.utils;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * des: 金钱交易相关的接口 需要RSA加密
 * author: lognyun
 */
public class RSAUtils {

    public static String publicKeys = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkUT6Z/qFey0NeqZnDvfXN3hzGL5Jy4nh" +
            "PhcrSTAXHr2aFxetlaTFMpHwVBnyIoTehAfCfxQ9UAT6wnxbqoWr8eNG2kP4FFCLSxApxQN0ddbN" +
            "uDrfp5ApUhdpBO94PDwK7Y/QWWbT0dFajxqMiFrwH0owO00SSDe6XbZiTFsR48KCbcjkZUCDvxsH" +
            "oo4P+13GaA0M8JPAIK8XeWjET0vzpT60bAtlBT23W5PO8N9X9hze38v6/rN9jgEps6Sx16lWEe0s" +
            "pCP227hqLW4nX9IhFVKdKX131mWovFkxEYW8ZshnI2pT/hdydEblwDtUGzrTZktZ8C1dzmF8+8dI" +
            "HsNYZQIDAQAB";
//    public static String privateKeys = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCRRPpn+oV7LQ16pmcO99c3eHMY\r\n" +
//            "vknLieE+FytJMBcevZoXF62VpMUykfBUGfIihN6EB8J/FD1QBPrCfFuqhavx40baQ/gUUItLECnF" +
//            "A3R11s24Ot+nkClSF2kE73g8PArtj9BZZtPR0VqPGoyIWvAfSjA7TRJIN7pdtmJMWxHjwoJtyORl" +
//            "QIO/Gweijg/7XcZoDQzwk8Agrxd5aMRPS/OlPrRsC2UFPbdbk87w31f2HN7fy/r+s32OASmzpLHX" +
//            "qVYR7SykI/bbuGotbidf0iEVUp0pfXfWZai8WTERhbxmyGcjalP+F3J0RuXAO1QbOtNmS1nwLV3O" +
//            "YXz7x0gew1hlAgMBAAECggEAIhNpHazhVMR7rfKMqDEw1b02Wm6AhvMf1929dJyIP9i/5XzTZg/R" +
//            "hgw/sKqpZrl+LMBZOk/4FeYlICvloLlcXRAwFZDuB23hTslK0wD9d4rclMOSpQ6m8q+1HchCjvaS" +
//            "pakfeWkqdMREFt9mabHTM7PGXpK0oCYaXFMk+b4/Ewq5z1DSV2CjUxih6wS6o7maqR3eRVyXm/HJ" +
//            "IGMfsArebgioq16POwo7UAUeO1phBvStcr6fovGQQTprnmI2uxZAqFLjVfiV2D1z8Otauo+TQRiB" +
//            "S9RBRom26Gc8uGvQbBxFbPUbuPvRfpl5rzc4r35zc9fQq5+okuPywJIOakLqXQKBgQDLJmfUVa/R" +
//            "4pkpDNhDzXajP51S67of54CwYsNzzZyp57mpHF5XTE3S9pwPgmsuQqYLEEKdRTUtQ/0+w/TypDly" +
//            "Dz/WAi+wGzHXfDSJuXYXFPOt9D2U3uinBIHgGBWOfEgkM6dAxKm7OIbEGuJQSgEKWslRHHLDaPpk" +
//            "J6OuUG2n/wKBgQC3D8hH9c3U0czn3bjEdivF13I300cVbpAV4zwvf5gIf0VaYI7RtpTUfKyB2+rn" +
//            "83tIlpEi8r3bRmP0peKV1LevJUWmBI4uua0v8/ax6tz3Y6yULfJRMY5VqWrjrC2SWVQ5H9iDJQ2w" +
//            "Zh17JFeATM1cf6N23CZPBHWbeeKjrPlfmwKBgAbNCFjRbYtnPJ1lMrBCZqBcdtXZ0mdTBrng9I4h" +
//            "6ga+HTcOHhJqGYjgfKUARrpLvOdN6gz9ziPvzMCXKtIcsVeiLw7pD9OjdW2zWMbrkIqBRw8hyBX9" +
//            "yGge/IIWDFKy0m9z0zuV2eDMqaZTCBSMBbDlDSxL+YOVNhy4PwCM7L7dAoGADOnJGAJn3Z5bFPQY" +
//            "B4vKdK9I+FUoC4P8F/8qjLM0d0z7CHANQw/dGcwdox9vR6NuS+WLIWr8J46kmGpnPGh/+JPpgNeL" +
//            "bBJe/CX6ffxyT+UxFFbVZBLZ1oMX7i2EHXG9tjXf4QhpGctqQviCT6ObTO4YCtdmwGYxIaGMcj1O" +
//            "bgcCgYBYuXfQbfm9Y1MXvt2AW15cw1pi5A2mzm2SGf8pduN5X+lJ2TIW4KVfojMHNzgENnIeVzYa" +
//            "GOQe+uNwHPCUaAHxOgdLthFiteDTf0uVwxl4AOZB8JAILPJj8TICD3L/RIAC0+hVU37QaHAjIJop" +
//            "0bGb8l0ouan2LN23GCtC7TfnNQ==";

    // 生成秘钥对
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    // 获取公钥(Base64编码)
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    // 获取私钥(Base64编码)
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    // 将Base64编码后的公钥转换成PublicKey对象
    public static PublicKey string2PublicKey(String pubStr) throws Exception {
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    // 将Base64编码后的私钥转换成PrivateKey对象
    public static PrivateKey string2PrivateKey(String priStr) throws Exception {
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    // 公钥加密
    public static byte[] publicEncrypt(byte[] content) throws Exception {
        PublicKey publicKey = string2PublicKey(publicKeys);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    // 私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }

    // 字节数组转Base64编码
    public static String byte2Base64(byte[] bytes) {
        return Base64Utils.encode(bytes);
    }

    // Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException {
        return Base64Utils.decode(base64Key);
    }

    /**
     * @param @param  byte2Base64
     * @param @return
     * @param @throws Exception    设定文件
     * @return String    返回类型
     * @throws
     * @Title: decode
     */
    public static String decode(String byte2Base64) throws Exception {
//        PrivateKey privateKey = string2PrivateKey(privateKeys);
//        // 加密后的内容Base64解码
//        byte[] base642Byte = base642Byte(byte2Base64);
//        // 用私钥解密
//        byte[] privateDecrypt = privateDecrypt(base642Byte, privateKey);
//        return new String(privateDecrypt, "UTF-8");
    return "";
    }


    public static String encode(String request) throws Exception {
        return Base64Utils.encode(publicEncrypt(request.getBytes()));
    }


}
