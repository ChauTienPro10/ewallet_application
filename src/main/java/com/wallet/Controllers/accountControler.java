package com.wallet.controllers;

import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.entitis.Member;
import com.wallet.entitis.User;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.UserRepository;


@RestController
@RequestMapping("/acc")
public class accountControler {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@PostMapping("/change")
	public String changeMail(@RequestBody Map<String, String> jsonData) {
		try {
			String email=jsonData.get("email");
			
			String password=jsonData.get("password");
			User user=userRepository.findByEmail(email);
			Member member=memberRepository.findByEmail(email);
			if(user==null || member==null) {
				return "this email not exist";
			}
			try {
				@SuppressWarnings("unused")
				Authentication authentication=authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(member.getUsername(),password) );
				String newemail=jsonData.get("newemail");
				if(newemail.equals(email)) {
					return "the new email similar with old one ";
				}
				member.setEmail(newemail);
				memberRepository.save(member);
				user.setEmail(newemail);
				userRepository.save(user);
				return "you are change email success!";
			}
			catch (Exception e) {
				return "can not authenticate for this email";
			}
			
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
		
	
	}
	
	
}
