package com.wallet.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.entitis.Card;
import com.wallet.entitis.Member;
import com.wallet.entitis.User;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.UserRepository;

@RestController
@RequestMapping("/admin")
public class AdminControler {
	@Autowired
	UserRepository userRepository;
	@Autowired 
	MemberRepository memberRepository;
	@Autowired 
	CardRepository cardRepository;
	@PostMapping("/delete")
	public String deleteUser(@RequestBody Map<String, String> jsonData) {
		try {
			String mail=jsonData.get("email");

			Member member=memberRepository.findByEmail(mail);
			if(member==null) {
				return "Email don't exist in the system";
			}
			
			User user =userRepository.findByEmail(mail);
			if(user!=null) {
				userRepository.delete(user);
			}
			Card card=cardRepository.getById(member.getMember_id());
			if(card!=null) {
				cardRepository.delete(card);
			}
			memberRepository.delete(member);
			return "this account was deleted";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "500 error";
		}
	}
}
