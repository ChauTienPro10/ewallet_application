package com.wallet;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptionExample {
    public static void main(String[] args) throws Exception {
        String privateKey = "This is the private key";
        String password = "MyPassword123";

        

        System.out.println("Encrypted Private Key: " + encryp(privateKey,password));
    }
    public static String encryp(String privateKey , String password) throws Exception {
    	

        byte[] salt = new byte[8];
       

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128); 
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        SecretKey secretKey = keyFactory.generateSecret(keySpec);

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); 
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] encryptedPrivateKey = cipher.doFinal(privateKey.getBytes());

        String encodedEncryptedPrivateKey = Base64.getEncoder().encodeToString(encryptedPrivateKey);

        return encodedEncryptedPrivateKey;
    }
}