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
	        // Lấy chuỗi base64 từ dữ liệu JSON được gửi tới
	        String base64 = jsonData.get("base64");
	        
	        // Giải mã chuỗi base64 thành mảng byte
	        byte[] byteArray = Base64.getDecoder().decode(base64);
	        
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member mem = memberRepository.findByUsername(username);
	        
	        // Đặt mảng byte làm avatar cho thành viên
	        mem.setAvatar(byteArray);
	        
	        // Lưu thành viên đã cập nhật vào cơ sở dữ liệu
	        memberRepository.save(mem);
	        
	        // Trả về chuỗi "OK" nếu thành công
	        return "OK";
	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện và in ra lỗi
	        e.printStackTrace();
	        
	        // Trả về thông báo lỗi nếu có lỗi xảy ra
	        return e.toString();
	    }
	}


	@PostMapping("/change_email")
	public String changeEmail(@RequestBody Map<String, String> jsonData) {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        
	        // Kiểm tra mật khẩu hiện tại có đúng không
	        if (!passwordEncoder.matches(jsonData.get("password"), member.getPassword())) {
	            return "password is incorrect: " + passwordEncoder.encode(jsonData.get("password"));
	        }
	        
	        // Lấy email mới từ dữ liệu JSON được gửi tới
	        String new_mail = jsonData.get("newemail");
	        
	        // Kiểm tra email mới có hợp lệ không
	        if (!Feature.isValidEmail(new_mail)) {
	            return "email is invalid";
	        }
	        
	        // Cập nhật email mới cho thành viên
	        member.setEmail(new_mail);
	        
	        // Lưu thành viên đã cập nhật vào cơ sở dữ liệu
	        memberRepository.save(member);
	        
	        // Trả về thông báo thành công
	        return "change email success!";
	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện
	        return "can not change your email";
	    }
	}


	@PostMapping("/change_phone")
	public String change_phone(@RequestBody Map<String, String> jsonData) {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        
	        // Kiểm tra mật khẩu hiện tại có đúng không
	        if (!passwordEncoder.matches(jsonData.get("password"), member.getPassword())) {
	            return "password is incorrect: " + passwordEncoder.encode(jsonData.get("password"));
	        }
	        
	        // Lấy số điện thoại mới từ dữ liệu JSON được gửi tới
	        String new_phone = jsonData.get("newphone");
	        
	        // Kiểm tra số điện thoại mới có hợp lệ không
	        if (!Feature.validatePhoneNumber(new_phone)) {
	            return "this phone is invalid";
	        }
	        
	        // Cập nhật số điện thoại mới cho thành viên
	        member.setPhone(new_phone);
	        
	        // Lưu thành viên đã cập nhật vào cơ sở dữ liệu
	        memberRepository.save(member);
	        
	        // Trả về thông báo thành công
	        return "change phone success!";
	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện và in ra lỗi
	        e.printStackTrace();
	        
	        // Trả về thông báo lỗi nếu có lỗi xảy ra
	        return "can't change your phone";
	    }
	}

	@PostMapping("/change_password")
	public String changePass(@RequestBody Map<String, String> jsonData) {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        
	        // Tìm người dùng (User) bằng email của thành viên
	        User us = userRepository.findByEmail(member.getEmail());
	        
	        // Kiểm tra mật khẩu hiện tại có đúng không
	        if (!passwordEncoder.matches(jsonData.get("password"), member.getPassword())) {
	            return "password is incorrect: " + passwordEncoder.encode(jsonData.get("password"));
	        }
	        
	        // Mã hóa mật khẩu mới
	        String hashPass = passwordEncoder.encode(jsonData.get("newpassword"));
	        
	        // Cập nhật mật khẩu mới cho thành viên và người dùng
	        member.setPassword(hashPass);
	        us.setPassword(hashPass);
	        
	        // Lưu người dùng đã cập nhật vào cơ sở dữ liệu
	        userRepository.save(us);
	        
	        // Lưu thành viên đã cập nhật vào cơ sở dữ liệu
	        memberRepository.save(member);
	        
	        // Trả về thông báo thành công
	        return "change password success!";
	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện và trả về thông báo lỗi
	        return "can't change your password";
	    }
	}

	@Autowired CardRepository cardRepository;
	@GetMapping("/getCard")
	public ResponseEntity<Card> getCard() {
	    // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    // Lấy tên người dùng từ thông tin xác thực
	    String username = authentication.getName();
	    
	    // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	    Member member = memberRepository.findByUsername(username);
	    
	    // Tìm thẻ của thành viên bằng ID của họ
	    Card card = cardRepository.findByMemberid(member.getMember_id());
	    
	    // Kiểm tra nếu thẻ tồn tại
	    if (card != null) {
	        // Trả về phản hồi HTTP 200 OK kèm thẻ trong cơ sở dữ liệu
	        return ResponseEntity.ok(card); 
	    }
	    
	    // Nếu không tìm thấy thẻ, trả về phản hồi null
	    return null;
	}

	@Autowired
	private EmailService emailService;
	@PostMapping("/get_new_pass")
	public String getNewPass(@RequestBody Map<String, String> jsonData) {
	    // Lấy email từ dữ liệu JSON được gửi tới
	    String email = jsonData.get("email");
	    
	    // Tìm thành viên trong cơ sở dữ liệu bằng email
	    Member member = memberRepository.findByEmail(email);
	    
	    // Tạo một số mật khẩu tạm thời ngẫu nhiên
	    SecureRandom secureRandom = new SecureRandom();
	    String tempPass = String.valueOf(100000 + secureRandom.nextInt(900000));
	    
	    // Tạo một đối tượng EmailDetails để gửi email với mật khẩu tạm thời
	    EmailDetails mail = new EmailDetails(member.getEmail(), tempPass, "Code From MUMU", null);
	    
	    // Gửi email thông qua dịch vụ email
	    String status = emailService.sendSimpleMail(mail);
	    
	    // Cập nhật mật khẩu của thành viên thành mật khẩu tạm thời
	    member.setPassword(tempPass);
	    memberRepository.save(member);
	    
	    // Trả về trạng thái của việc gửi email
	    return status;
	}

	
	@PostMapping("/authenCode_change_pass")
	public String authen(@RequestBody Map<String, String> jsonData) {
	    // Lấy email từ dữ liệu JSON được gửi tới
	    String email = jsonData.get("email");
	    
	    // Tìm thành viên trong cơ sở dữ liệu bằng email
	    Member member = memberRepository.findByEmail(email);
	    
	    // Tìm người dùng (User) trong cơ sở dữ liệu bằng email
	    User user = userRepository.findByEmail(email);
	    
	    // So sánh mã xác thực với mật khẩu của thành viên
	    if (member.getPassword().equals(jsonData.get("code"))) {
	        // Mã hóa mật khẩu mới
	        String hashPass = passwordEncoder.encode(jsonData.get("newpassword"));
	        
	        // Cập nhật mật khẩu mới cho thành viên và người dùng
	        member.setPassword(hashPass);
	        user.setPassword(hashPass);
	        
	        // Lưu thành viên và người dùng đã cập nhật vào cơ sở dữ liệu
	        memberRepository.save(member);
	        userRepository.save(user);
	        
	        // Trả về "ok" để chỉ ra rằng xác thực đã thành công
	        return "ok";
	    }
	    
	    // Trả về "no" để chỉ ra rằng xác thực không thành công
	    return "no";
	}

}
