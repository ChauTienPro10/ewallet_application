package com.wallet.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.Entitis.Member;
import com.wallet.Entitis.User;
import com.wallet.Repositories.MemberRepository;
import com.wallet.Repositories.UserRepository;
import com.wallet.Utls.Feature;

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
