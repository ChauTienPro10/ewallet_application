package com.wallet.utls;

import java.util.Random;

public class RandomStringExample {
	public static String create_codeTrans()  {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int length = 20;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        String randomString = sb.toString();
       return randomString;
    }
}
