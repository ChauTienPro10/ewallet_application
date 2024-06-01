package com.wallet.controllers;

import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.EncryptionExample;
import com.wallet.entitis.BeanData;
import com.wallet.entitis.Card;
import com.wallet.entitis.CustomUserDetail;
import com.wallet.entitis.EmailDetails;
import com.wallet.entitis.EtherWallet;
import com.wallet.entitis.LoginRequest;
import com.wallet.entitis.LoginResponse;
import com.wallet.entitis.Member;
import com.wallet.entitis.User;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.EmailService;
import com.wallet.repositories.EthereumRepository;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.UserRepository;
import com.wallet.utls.Feature;
import com.wallet.web3j.MyContractWrapper;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

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
	@Autowired
	EthereumRepository ethereumRepository;


	@GetMapping("/hello")
	public String getHelo() {
		return "helo world";
	}

	@Autowired
	PasswordEncoder passwordEncoder;

	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "Spring-BeanData.xml" });

	// xu ly dang nhap ben duoi
	@PostMapping("/login")
	public LoginResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws AuthenticationException {
//xac thuc thong tin nguoi dung 
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = tokenProvider.generateToken((CustomUserDetail) authentication.getPrincipal());// tao  jwt 
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
		User us = userRepository.findByUsername(loginRequest.getUsername());
		return new LoginResponse(jwt, authorities, authentication.getName(), us.getUser_id());// tra ve thong tin nguoi dung da xac thuc kem theo mot chuoi jwt
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {

		SecurityContextHolder.clearContext();// xoa tat ca thong tin nguoi dung khoi phien hien tai

		return ResponseEntity.ok().build();
	}

	// xu ly dang ky ben duoi
	@PostMapping("/register")
	public String register(@RequestBody Map<String, String> jsonData) {
	
		try {
			// lay thong tin tu yeu cau
			String first_name = jsonData.get("fname");
			String last_name = jsonData.get("lname");
			String email = jsonData.get("email");
			
			// kiem tra email da duoc dung hay chua
			if (memberRepository.findByEmail(email) != null) {
				return "this email has used";
			}
			// kiem tra email co hop le hay khong
			if (Feature.isValidEmail(email) == false) {
				return "this email invalid! Example for an email : [example123@gmail.com]";
			}
			String country = jsonData.get("country");

			String contact = jsonData.get("phone");
			String username = jsonData.get("username");
			// kiem tra username da duoc dung hay chua
			if (memberRepository.findByUsername(username) != null) {
				return "this username was used";
			}
// kiem tra d dai mat khau co phu hop hay khong
			String password = jsonData.get("password");
			if (password.length() < 6 || password.length() > 12) {
				return "Password must be larger than 6 characters and less than 13 characters";
			}
			if (password.equals(password.toLowerCase()) == true) {
				return "password must be has lessest a higher case";
			}
			String hashPass = passwordEncoder.encode(password);
			
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

	



	@Autowired
	private EmailService emailService;

	@PostMapping("/sendMail")
	public String sendMail() {
		EmailDetails mail = new EmailDetails("itchauduongphattien@gmail.com", "xin chao", "hello", null);
		String status = emailService.sendSimpleMail(mail);

		return status;
	}

	@PostMapping("/handle_forget_pass")
	public String getNewPass(@RequestBody Map<String, String> jsonData) {
		try {
			String email = jsonData.get("email");
			User user = userRepository.findByEmail(email);
			if (user == null) {
				return "this email not exist";
			}
			String code = "220103";
			EmailDetails mail = new EmailDetails(email, code, "Authenticate", null);
			String status = emailService.sendSimpleMail(mail);

			BeanData beandata = (BeanData) context.getBean("BeanData");
			beandata.setCodeChangeMail(code);
			return "the authentication code has given to your email";
		} catch (Exception e) {
			return e.toString();
		}

	}

	@PostMapping("/CheckCodeMail")
	public Boolean getsession(@RequestBody Map<String, String> jsonData) {
		String code = jsonData.get("code");
		BeanData beandata = (BeanData) context.getBean("BeanData");
		if (code.equals(beandata.getCodeChangeMail())) {
			return true;
		}
		return false;

	}

	@Autowired
	CardRepository cardRepository;

	@PostMapping("/authen_pincode")
	public Boolean authen_pincode(@RequestBody Map<String, String> jsonData) {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Lấy mã PIN từ dữ liệu JSON được gửi tới
	        String pincode = jsonData.get("pincode");
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        
	        // Tìm thẻ của thành viên bằng ID của họ
	        Card card = cardRepository.findByMemberid(member.getMember_id());
	        
	        // So sánh mã PIN của thẻ với mã PIN được gửi tới
	        if (card.getPin().equals(pincode)) {
	            return true;
	        }
	        
	        // Trả về false nếu mã PIN không khớp
	        return false;
	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện và in ra lỗi
	        e.printStackTrace();
	        
	        // Trả về false nếu có lỗi xảy ra
	        return false;
	    }
	}

	
	@GetMapping("/getPincode")
	public String getPin() {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        
	        // Tìm thẻ (Card) của thành viên bằng ID của họ
	        Card card = cardRepository.findByMemberid(member.getMember_id());
	        
	        // Trả về mã PIN của thẻ
	        return card.getPin();
	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện và trả về thông báo lỗi
	        return "can't authenticate";
	    }
	}


	@PostMapping("/linkWallet")// xu ly lien ket vi ETH
	public String linkToEwallet(@RequestBody Map<String, String> jsonData) {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);

	        // Lấy địa chỉ ví Ethereum từ dữ liệu JSON
	        String address = jsonData.get("address");
	        
	        // Lấy mã PIN từ dữ liệu JSON
	        String password = jsonData.get("pin");

	        // Lấy khóa riêng từ dữ liệu JSON
	        String privateKey = jsonData.get("key");
	        
	        // Mã hóa khóa riêng bằng mật khẩu (PIN)
	        String hash = EncryptionExample.encryp(privateKey, password); // mã hóa khóa theo mật khẩu
	        
	        // Tạo một instance của MyContractWrapper với khóa riêng
	        MyContractWrapper myContractWrapper = new MyContractWrapper(privateKey);
	        
	        // Kiểm tra tính hợp lệ của khóa riêng và địa chỉ ví Ethereum
	        if (myContractWrapper.isPrivateKeyValid(privateKey, address)) {
	            // Tạo một đối tượng EtherWallet mới với thông tin của thành viên, địa chỉ và khóa đã mã hóa
	            EtherWallet etherWallet = new EtherWallet(member.getMember_id(), address, hash);
	            
	            // Lưu đối tượng EtherWallet vào cơ sở dữ liệu
	            ethereumRepository.save(etherWallet);
	            
	            // Trả về thông báo liên kết thành công
	            return "link success!";
	        } else {
	            // Trả về thông báo nếu không thể liên kết với địa chỉ này
	            return "can't link to this address";
	        }

	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện và in ra lỗi
	        e.printStackTrace();
	        
	        // Trả về thông báo lỗi liên kết
	        return "can't link to ETH account!";
	    }
	}


}
