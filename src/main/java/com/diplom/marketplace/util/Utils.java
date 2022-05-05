package com.diplom.marketplace.util;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class Utils {

    public static String getSaltString(int len) {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < len) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String checksum(String key, String message) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] hmacArray = sha256_HMAC.doFinal(message.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder(hmacArray.length * 2);
        for (byte b : hmacArray)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }


    public static String encrypt(String plainText, String aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec("Nubisoft1234%^&*".getBytes()));
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")));
    }

    public static String decrypt(byte[] cipherText, String aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(aesKey.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec("Nubisoft1234%^&*".getBytes()));
        return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }

    public static HashMap<String, String> jsonToMap(String t) throws Exception {

        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }

        return map;
    }
}
