package com.wallet.Controllers;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.Entitis.BeanData;
import com.wallet.Entitis.CustomUserDetail;
import com.wallet.Entitis.EmailDetails;
import com.wallet.Entitis.LoginRequest;
import com.wallet.Entitis.LoginResponse;
import com.wallet.Entitis.Member;
import com.wallet.Entitis.User;
import com.wallet.Repositories.EmailService;
import com.wallet.Repositories.MemberRepository;
import com.wallet.Repositories.UserRepository;
import com.wallet.Utls.Feature;

import ch.qos.logback.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RestController
@RequestMapping("/test")
public class TestControler {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	private com.wallet.JwtTokenProvider tokenProvider;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MemberRepository memberRepository;

	@GetMapping("/hello")
	public String getHelo() {
		return "helo world";
	}

	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	ApplicationContext context = 
           	 new ClassPathXmlApplicationContext(new String[] {"Spring-BeanData.xml"});

	@PostMapping("/login")
	public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws AuthenticationException {
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
		User us = userRepository.findByUsername(loginRequest.getUsername());
		return new LoginResponse(jwt, authorities, authentication.getName(), us.getUser_id());
	}

	@PostMapping("/register")
	public String register(@RequestBody Map<String, String> jsonData) {
//		
//		{
//			  
//			  "first_name": "Chau Duong Phat",
//			  "last_name": "Tien",
//			  "email_address": "tien1@gmail.com",
//			  "country": "VIETNAM",
//			  "contact_number": "0812788212",
//			  "username": "tien2201",
//			  "password": "Tien22012003",
//			  "account_status": "0"
//			}
//		
		try {
			String first_name = jsonData.get("first_name");
			String last_name = jsonData.get("last_name");
			String email = jsonData.get("email_address");
			if (memberRepository.findByEmail(email) != null) {
				return "this email has used";
			}
			if (Feature.isValidEmail(email) == false) {
				return "this email invalid! Example for an email : [example123@gmail.com]";
			}
			String country = jsonData.get("country");

			String contact = jsonData.get("contact_number");
			String username = jsonData.get("username");
			if (memberRepository.findByUsername(username) != null) {
				return "this username was used";
			}

			String password = jsonData.get("password");
			if (password.length() < 6 || password.length() > 12) {
				return "Password must be larger than 6 characters and less than 13 characters";
			}
			if (password.equals(password.toLowerCase()) == true) {
				return "password must be has lessest a higher case";
			}
			String hashPass = passwordEncoder.encode(password);
			System.out.println(hashPass);
			int account_status = Integer.parseInt(jsonData.get("account_status"));

			Member member = new Member(first_name, last_name, email, country, contact, username, hashPass,
					account_status);
			User user = new User(username, hashPass, first_name + last_name, email);
			memberRepository.save(member);
			userRepository.save(user);

			return "you are regis an new account";

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@PostMapping("/change_password")
	public String changePass(@RequestBody Map<String, String> jsonData) {
		try {
			User user = userRepository.findByUsername(jsonData.get("username"));
			if (user != null) {
				String encodedPassword = passwordEncoder.encode(jsonData.get("newPassword"));
				user.setPassword(encodedPassword);
				userRepository.save(user);
				return "You reseted password";
			} else {
				return "Not found username";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}

	}

	@PostMapping("/checkPassword")
	public String checkPass(@RequestBody Map<String, String> jsonData) {
		try {
			String username = jsonData.get("username");
			System.out.println(jsonData.get("password"));
			String pass = jsonData.get("password");
			
			
			try {
				Authentication authentication=authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(username,pass) );
				return "OK";
			}
			catch (Exception e) {
				e.printStackTrace();
				return "Authentivate failue";
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	@Autowired private EmailService emailService;
	@PostMapping("/sendMail")
    public String
    sendMail()
    {
		EmailDetails mail=new EmailDetails("itchauduongphattien@gmail.com", "xin chao", "hello", null);
        String status
            = emailService.sendSimpleMail(mail);
 
        return status;
    }
	
	@PostMapping("/handle_forget_pass")
	public String getNewPass(@RequestBody Map<String, String> jsonData) {
		try {
			String email=jsonData.get("email");
			User user=userRepository.findByEmail(email);
			if(user==null) {
				return "this email not exist";
			}
			String code="220103";
			EmailDetails mail=new EmailDetails(email, code, "Authenticate", null);
	        String status
	            = emailService.sendSimpleMail(mail);
	        
			BeanData beandata=(BeanData)context.getBean("BeanData");
			beandata.setCodeChangeMail(code);
			return "the authentication code has given to your email";
		}catch (Exception e) {
			return e.toString();
		}
		
		
	}
	
	@PostMapping("/CheckCodeMail")
	public Boolean getsession(@RequestBody Map<String, String> jsonData) {
			String code=jsonData.get("code");
			BeanData beandata=(BeanData)context.getBean("BeanData");
			if(code.equals(beandata.getCodeChangeMail())) {
				return true;
			}
			return false;
		
	}
	
	

}
