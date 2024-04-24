package com.wallet;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class DecryptionExample {
    public static void main(String[] args) throws Exception {
        String encodedEncryptedPrivateKey = "QLC+b6BP9Lv2sevuobmjQdc7Vc2eehP3MwQWstrrQlo=";
        String password = "MyPassword123";
        System.out.print(decryp(encodedEncryptedPrivateKey,password));        
    }
    public static String decryp(String hash, String password) throws Exception {
    	byte[] salt = new byte[8];


        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] encryptedPrivateKey = Base64.getDecoder().decode(hash);
        byte[] decryptedPrivateKey = cipher.doFinal(encryptedPrivateKey);

        String privateKey = new String(decryptedPrivateKey);

       return privateKey;
    }
}
