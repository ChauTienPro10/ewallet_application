package com.wallet.controllers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.wallet.DecryptionExample;
import com.wallet.entitis.Card;
import com.wallet.entitis.Deposit;
import com.wallet.entitis.EtherWallet;
import com.wallet.entitis.Member;
import com.wallet.entitis.Transaction_block;
import com.wallet.entitis.Withdrawal;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.EthereumRepository;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.WithdrawalRepository;
import com.wallet.state.CardState;
import com.wallet.web3j.MyContractWrapper;

@RestController
@RequestMapping("/ETH")
public class EtherTransControler {
	@Autowired MemberRepository memberRepository;
	@Autowired EthereumRepository ethereumRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired TransactionRepository transactionRepository;
	@Autowired CardRepository cardRepository;
	@Autowired DepositRepository depositRepository;
	@Autowired WithdrawalRepository withdrawalRepository;
	@PostMapping("/transfer") // xu ky chuyen tien
	public String transfer(@RequestBody Map<String, String> jsonData) {
		try {
			// nhan du lieu tu yeu cau
			String toAddress=jsonData.get("receiver");
			double amount=Double.parseDouble(jsonData.get("amount"));
			amount=amount*Math.pow(10, 18); // chuyen ve so gas bnag cach x cho 10 luy thua 18
			System.out.print(BigDecimal.valueOf(amount).toBigInteger());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member=memberRepository.findByUsername(username);
			EtherWallet etherWallet=ethereumRepository.findByMemberid(member.getMember_id()); // kiem tra xem nguoi dung co lien ket vi hay chua
			if(etherWallet==null) {
				return "user have not yet link to ETH";
			}
			String password=jsonData.get("pin");
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			String key=DecryptionExample.decryp(etherWallet.getKey(), password); 
			System.out.print(key);
			MyContractWrapper myContractWrapper=new MyContractWrapper(key);
			if (myContractWrapper.isPrivateKeyValid(key, etherWallet.getAddress())==true) { // kieu  tra khoa co hop le hay khong
				RemoteFunctionCall<TransactionReceipt> remoteCall 
				= myContractWrapper.transferETH(toAddress,BigDecimal.valueOf(amount).toBigInteger());
			
		
				TransactionReceipt transactionReceipt = remoteCall.send();
				return "transfer success";
			}
			return "error";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "can't transfer";
		}
	}
	
	@PostMapping("deposit_by_Eth")
	public String depositEthtoMoney(@RequestBody Map<String, String>jsonData) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();// xac thuc nguoi dung
			String username = authentication.getName();
			Member member=memberRepository.findByUsername(username);
			BigDecimal eth=MyContractWrapper.convertVndToEther(new BigDecimal(jsonData.get("amount").replaceAll(",", "")));// thay the tat ca ky tu dau , thanh ky tu dau .
			double amount=eth.doubleValue();
			amount=amount*Math.pow(10, 18);// chuyen ve so gas
			
			String toAddress="0x09283041db45446836D18180a61943fBeA403C72";
			EtherWallet etherWallet=ethereumRepository.findByMemberid(member.getMember_id());
			if(etherWallet==null) {
				return "user have not yet link to ETH";
			}
			String password=jsonData.get("pin");
			String key=DecryptionExample.decryp(etherWallet.getKey(), password);// giai ma khoa de kiem tra tinh hop le cua khoa
			MyContractWrapper myContractWrapper=new MyContractWrapper(key);
			if (myContractWrapper.isPrivateKeyValid(key, etherWallet.getAddress())==true) {
				RemoteFunctionCall<TransactionReceipt> remoteCall 
				= myContractWrapper.transferETH(toAddress,BigDecimal.valueOf(amount).toBigInteger());
				TransactionReceipt transactionReceipt = remoteCall.send();
				BigDecimal vndAmount=MyContractWrapper.convertEtherToVnd(BigDecimal.valueOf(amount));
				int stt = 0;
				String preHash = null;
				if (transactionRepository.count() != 0) {
					stt = (int) transactionRepository.count();
					Transaction_block transaction = transactionRepository.findByBlockid(stt);

					preHash = transaction.getHash_block();
				}
				Card card = cardRepository.findByMemberid(member.getMember_id());
				if (card == null) {
					return "The account has not yet activated the card ";
				}
				CardState state=member.initState(); // lay trang thai cua the dang su dung hoac bi khoa
				state.doTransfer(member.getMember_id(),new BigDecimal(jsonData.get("amount").replaceAll(",", "")), stt, preHash, card, cardRepository, depositRepository, transactionRepository);
	
				return "OK";
			}
			return "NO";
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	@PostMapping("/withdraw")// xu ly rut tien
	public String withdraw(@RequestBody Map<String, String>jsonData) {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        // Tìm ví Ethereum của thành viên bằng ID của họ
	        EtherWallet etherWallet = ethereumRepository.findByMemberid(member.getMember_id());
	        // Chuyển đổi số tiền từ VND sang ETH
	        BigDecimal eth = MyContractWrapper.convertVndToEther(new BigDecimal(jsonData.get("amount").replaceAll(",", "")));
	        // Chuyển đổi số tiền từ BigDecimal sang double
	        double amount = eth.doubleValue();
	        // Chuyển đổi số tiền sang đơn vị wei (1 ETH = 10^18 wei)
	        amount = amount * Math.pow(10, 18);
	        // Nếu ví Ethereum của người dùng không tồn tại
	        if (etherWallet == null) {
	            return "user have not yet link to ETH";
	        }
	        // Tìm thông tin của admin bằng ID
	        Member memberAdmin = memberRepository.findByMemberid(9);
	        // Tìm ví Ethereum của admin bằng ID
	        EtherWallet etherWalletAdmin = ethereumRepository.findByMemberid(9);
	        // Mật khẩu của admin
	        String passwordadmin = "112233";
	        // Giải mã khóa riêng của admin bằng mật khẩu
	        String keyadmin = DecryptionExample.decryp(etherWalletAdmin.getKey(), passwordadmin);

	        // Lấy mật khẩu (PIN) từ dữ liệu JSON
	        String password = jsonData.get("pin");
	        // Giải mã khóa riêng của người dùng bằng mật khẩu
	        String key = DecryptionExample.decryp(etherWallet.getKey(), password);
	        // Tạo một instance của MyContractWrapper với khóa riêng của admin
	        MyContractWrapper myContractWrapperAd = new MyContractWrapper(keyadmin);

	        // Kiểm tra tính hợp lệ của khóa riêng và địa chỉ ví Ethereum của người dùng
	        if (myContractWrapperAd.isPrivateKeyValid(key, etherWallet.getAddress()) == true) {
	            try {
	                // Tạo một cuộc gọi từ xa để chuyển ETH
	                RemoteFunctionCall<TransactionReceipt> remoteCall 
	                    = myContractWrapperAd.transferETH(etherWallet.getAddress(), BigDecimal.valueOf(amount).toBigInteger());
	                // Gửi giao dịch và nhận biên nhận
	                TransactionReceipt transactionReceipt = remoteCall.send();
	                // Khởi tạo trạng thái thẻ của người dùng
	                CardState state = member.initState();
	                // Thực hiện việc rút tiền và cập nhật trạng thái
	                state.dowithdraw(member, cardRepository, new BigDecimal(jsonData.get("amount").replaceAll(",", "")), transactionRepository, withdrawalRepository);
	                return "OK";
	            } catch (Exception e) {
	                // Bắt lỗi nếu có lỗi xảy ra trong quá trình chuyển ETH
	                e.printStackTrace();
	                return "NO";
	            }
	        }
	        // Trả về "NO" nếu khóa riêng không hợp lệ
	        return "NO";
	    } catch (Exception e) {
	        // Bắt và trả về lỗi nếu có lỗi xảy ra trong quá trình thực hiện
	        return e.toString();
	    }
	}

	
	@GetMapping("/getEth")
	public ResponseEntity<EtherWallet> getInfWallet() {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        
	        // Tìm ví Ethereum của thành viên bằng ID của họ
	        EtherWallet eth = ethereumRepository.findByMemberid(member.getMember_id());
	        
	        // Kiểm tra nếu ví Ethereum tồn tại
	        if (eth != null) {
	            // Trả về phản hồi HTTP 200 (OK) kèm theo thông tin ví Ethereum
	            return ResponseEntity.ok(eth);
	        } else {
	            // Trả về phản hồi HTTP 404 (Not Found) nếu ví Ethereum không tồn tại
	            return ResponseEntity.notFound().build();
	        }
	    } catch (Exception e) {
	        // Bắt và trả về phản hồi HTTP 404 (Not Found) nếu có lỗi xảy ra
	        return ResponseEntity.notFound().build();
	    }
	}

	@PostMapping("/getEthBalance")
	public String getbalance(@RequestBody Map<String, String> jsonData) {
	    try {
	        // Lấy thông tin xác thực hiện tại từ SecurityContextHolder
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        
	        // Lấy tên người dùng từ thông tin xác thực
	        String username = authentication.getName();
	        
	        // Tìm thành viên trong cơ sở dữ liệu bằng tên người dùng
	        Member member = memberRepository.findByUsername(username);
	        
	        // Tìm ví Ethereum của thành viên bằng ID của họ
	        EtherWallet eth = ethereumRepository.findByMemberid(member.getMember_id());
	        
	        // Lấy mã PIN từ dữ liệu JSON được gửi tới
	        String pass = jsonData.get("pin");
	        
	        // Giải mã khóa riêng của ví Ethereum bằng mã PIN
	        String key = DecryptionExample.decryp(eth.getKey(), pass);
	        
	        // Tạo một instance của MyContractWrapper với khóa riêng đã giải mã
	        MyContractWrapper myContractWrapperAd = new MyContractWrapper(key);
	        
	        // In ra địa chỉ ví Ethereum của thành viên
	        System.out.println(eth.getAddress());
	        
	        // Lấy số dư tài khoản của ví Ethereum và trả về dưới dạng chuỗi
	        return myContractWrapperAd.getAccountBalance(eth.getAddress()).toString().substring(0, 4);
	    } catch (Exception e) {
	        // Bắt lỗi nếu có lỗi xảy ra trong quá trình thực hiện và in ra lỗi
	        e.printStackTrace();
	        
	        // Trả về chuỗi "error" nếu có lỗi xảy ra
	        return "error";
	    }
	}


}
