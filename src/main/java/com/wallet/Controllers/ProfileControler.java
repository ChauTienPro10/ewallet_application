package com.wallet.controllers;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.entitis.Member;
import com.wallet.repositories.MemberRepository;
import com.wallet.utls.Feature;

@RestController
@RequestMapping("/profile")
public class ProfileControler {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/changeAVT")
	public String changeavt(@RequestBody Map<String, String> jsonData) {
		try {
			String base64 = jsonData.get("base64");
			byte[] byteArray = Base64.getDecoder().decode(base64);
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member mem = memberRepository.findByUsername(username);
			mem.setAvatar(byteArray);
			memberRepository.save(mem);
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@PostMapping("/change_email")
	public String changeEmail(@RequestBody Map<String, String> jsonData) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member = memberRepository.findByUsername(username);
			if (passwordEncoder.matches(jsonData.get("password"), member.getPassword())==false) {
				return "password is incorrect: " + passwordEncoder.encode(jsonData.get("password"));
			}
			String new_mail = jsonData.get("newemail");
			if (Feature.isValidEmail(new_mail) == false) {
				return "email is invalid";
			}
			member.setEmail(new_mail);
			memberRepository.save(member);
			return "change email success!";

		} catch (Exception e) {

			return "can not change your email";
		}
	}

	@PostMapping("/change_phone")
	public String change_phone(@RequestBody Map<String, String> jsonData) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member = memberRepository.findByUsername(username);
			if (passwordEncoder.matches(jsonData.get("password"), member.getPassword())==false) {
				return "password is incorrect: " + passwordEncoder.encode(jsonData.get("password"));
			}
			String new_phone = jsonData.get("newphone");
			if (Feature.validatePhoneNumber(new_phone) == false) {
				return "this phone is invalid";
			}
			member.setPhone(new_phone);
			memberRepository.save(member);
			return "change phone success!";
		} catch (Exception e) {
			e.printStackTrace();
			return "can't change your phone";
		}
	}
	@PostMapping("/change_password")
	public String changePass(@RequestBody Map<String, String> jsonData) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member = memberRepository.findByUsername(username);
			if (passwordEncoder.matches(jsonData.get("password"), member.getPassword())==false) {
				return "password is incorrect: " + passwordEncoder.encode(jsonData.get("password"));
			}
			String hashPass = passwordEncoder.encode(jsonData.get("newpassword"));
			member.setPassword(hashPass);
			memberRepository.save(member);
			return "change password success!";
			
		}catch (Exception e) {
			return "can't change your password";
		}
	}
}
