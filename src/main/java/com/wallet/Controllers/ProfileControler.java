package com.wallet.controllers;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.entitis.Card;
import com.wallet.entitis.EmailDetails;
import com.wallet.entitis.Member;
import com.wallet.entitis.User;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.EmailService;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.UserRepository;
import com.wallet.utls.Feature;

@RestController
@RequestMapping("/profile")
public class ProfileControler {
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired UserRepository userRepository;

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
			User us=userRepository.findByEmail(member.getEmail());
			if (passwordEncoder.matches(jsonData.get("password"), member.getPassword())==false) {
				return "password is incorrect: " + passwordEncoder.encode(jsonData.get("password"));
			}
			String hashPass = passwordEncoder.encode(jsonData.get("newpassword"));
			member.setPassword(hashPass);
			us.setPassword(hashPass);
			userRepository.save(us);
			memberRepository.save(member);
			return "change password success!";
			
		}catch (Exception e) {
			return "can't change your password";
		}
	}
	
	@Autowired CardRepository cardRepository;
	@GetMapping("/getCard")
	public ResponseEntity<Card> getCard(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		Member member = memberRepository.findByUsername(username);
		Card card=cardRepository.findByMemberid(member.getMember_id());
		if(card !=null) {
			return ResponseEntity.ok(card); 
		}
		return null;
	}
	@Autowired
	private EmailService emailService;
	@PostMapping("/get_new_pass")
	public String getNewPass(@RequestBody Map<String, String>jsonData) {
			String Email=jsonData.get("email");
			
			Member member = memberRepository.findByEmail(Email);
			SecureRandom secureRandom = new SecureRandom();
	        String tempPassr = String.valueOf(100000 + secureRandom.nextInt(900000));
			EmailDetails mail = new EmailDetails(member.getEmail(), tempPassr, "Code From MUMU", null);
			String status = emailService.sendSimpleMail(mail);
			member.setPassword(tempPassr);
			memberRepository.save(member);
			return status;

	}
	
	@PostMapping("/authenCode_change_pass")
	public String authen(@RequestBody Map<String, String>jsonData) {
		String Email=jsonData.get("email");
		Member member = memberRepository.findByEmail(Email);
		User us=userRepository.findByEmail(Email);
		if(member.getPassword().equals(jsonData.get("code"))) {
			String hashPass = passwordEncoder.encode(jsonData.get("newpassword"));
			member.setPassword(hashPass);
			us.setPassword(hashPass);
			memberRepository.save(member);
			userRepository.save(us);
			return "ok";
		}
		return "no";
	}
}
