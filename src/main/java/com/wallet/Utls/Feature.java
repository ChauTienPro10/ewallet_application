package com.wallet.Utls;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.google.gson.Gson;
import com.wallet.Entitis.Deposit;
import com.wallet.Entitis.Recharge;
import com.wallet.Entitis.Transaction_block;
import com.wallet.Entitis.Transfer;
import com.wallet.Entitis.Withdrawal;

import ch.qos.logback.core.boolex.Matcher;

public class Feature {
	
	
	public static boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
	
	public static boolean authentication_check(String username, String password,AuthenticationManager authenticationManager) {
		try {
			try {
				Authentication authentication=authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(username,password) );
				return true;
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static String getJsonObjectTranSaction(Transaction_block transaction_block) {
    	Gson gson=new Gson();
    	return gson.toJson(transaction_block);
    }
	public static String getJsonObjectDeposit(Deposit deposit) {
    	Gson gson=new Gson();
    	return gson.toJson(deposit);
    }
	public static String getJsonObjectWithdraw(Withdrawal withdraw) {
    	Gson gson=new Gson();
    	return gson.toJson(withdraw);
    }
	public static String getJsonObjectTrasfer(Transfer withdraw) {
    	Gson gson=new Gson();
    	return gson.toJson(withdraw);
    }
	public static String getJsonObjectRecharge(Recharge recharge) {
    	Gson gson=new Gson();
    	return gson.toJson(recharge);
    }
	
	public static  String calculateSHA256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return null;
    }
	
	public static boolean validatePhoneNumber(String phoneNumber) {
        // Mẫu số điện thoại: 10 chữ số bắt đầu bằng 0
        String pattern = "^0\\d{9}$";
        
        Pattern regexPattern = Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regexPattern.matcher(phoneNumber);
        
        return matcher.matches();
    }
}
