package com.wallet.controllers;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kenai.jnr.x86asm.Mem;
import com.wallet.entitis.Deposit;
import com.wallet.entitis.History_tranfer;
import com.wallet.entitis.Member;
import com.wallet.entitis.Recharge;
import com.wallet.entitis.Transaction_block;
import com.wallet.entitis.Transfer;
import com.wallet.entitis.Withdrawal;
import com.wallet.factory.DepositImpl;
import com.wallet.factory.Transaction;
import com.wallet.factory.TransferIml;
import com.wallet.factory.WithdrawImpl;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.RechargeRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.TransferRepository;
import com.wallet.repositories.WithdrawalRepository;
;

@RestController
@RequestMapping("/check")
public class CheckControler {
	
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	TransferRepository transferRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired DepositRepository depositRepository;
	@Autowired WithdrawalRepository withdrawalRepository;
	@Autowired RechargeRepository rechargeRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	
	
	//nhan 20 giao dich gan nhat
	@GetMapping("/history")
	public List<History_tranfer> getAllTransactions() {
	    List<History_tranfer> historis = new ArrayList<>();
	    try {
	    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member mem=memberRepository.findByUsername(username);
	        List<Transaction_block> trans = transactionRepository.findByMemberid(mem.getMember_id());
	        int startIndex = Math.max(trans.size() - 10, 0);
	        trans=trans.subList(startIndex, trans.size());

	        for (int i=trans.size()-1; i>=0;i--) {
	            History_tranfer ht = createHistoryTransfer(trans.get(i));
	            if (ht != null) {
	                historis.add(ht);
	            }
	        }

	        return historis.isEmpty() ? null : historis;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	// tim lich su giao dich theo tu khoa
	@PostMapping("/findHistory")
	public List<History_tranfer> find(@RequestBody Map<String, String>jsonData){
		List<History_tranfer> historis = new ArrayList<>();
		try {
			String content=jsonData.get("content");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member mem=memberRepository.findByUsername(username);
	        List<Transaction_block> trans = transactionRepository.findByMemberid(mem.getMember_id());

	        for (Transaction_block transaction : trans) {
	            History_tranfer ht = createHistoryTransfer(transaction);
	            if (ht != null && ht.getContent().contains(content)) {
	                historis.add(ht);
	            }
	        }
	        HashSet<History_tranfer> set = new HashSet<History_tranfer>(historis);
	        List<History_tranfer> uniqueList = new ArrayList<History_tranfer>(set);
	        return uniqueList.isEmpty() ? null : historis;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	// ham khoi tao lich su giao dich
	private History_tranfer createHistoryTransfer(Transaction_block transaction) {
	    History_tranfer ht = new History_tranfer();
	    int transactionType = transaction.getTransaction_type();
	    String transactionCode = transaction.getTransaction_code();

	    switch (transactionType) {
	        case 1:
	        	Transaction deposit=new DepositImpl();
	        	deposit.get_repository(depositRepository,memberRepository);
	        	return deposit.setHistory_transaction(transaction);


	        case 2:

	        	
	        	Transaction withdraw=new WithdrawImpl();
	        	withdraw.get_repository(withdrawalRepository, memberRepository);
	        	return withdraw.setHistory_transaction(transaction);
	        	

	        case 3:
          
	        	Transaction transferImpl=new TransferIml();
	        	transferImpl.get_repository(transferRepository, memberRepository);
	        	return transferImpl.setHistory_transaction(transaction);

	        case 4:
	            Recharge recharge = rechargeRepository.findByTransactioncode(transactionCode);
	            if (recharge != null) {
	                ht.setType("recharge telephone");
	                ht.setTime(recharge.getTime());
	                ht.setAmount(recharge.getAmount());
	                ht.setContent("You recharged " + recharge.getAmount() + " to " + recharge.getPhone());
	                return ht;
	            }
	            break;
	    }

	    return null;
	}

	
	//tim nguoi dung theo thong tin
	@PostMapping("/find_member")
	public ResponseEntity<Member> findmember(@RequestBody Map<String, String> jsonData){
		try {
			String infor=jsonData.get("infor_data");
			// tim theo username
			if(memberRepository.findByUsername(infor)!=null) {
				Member mem=memberRepository.findByUsername(infor);
				return ResponseEntity.ok(mem);
			}
//			tim theo email
			else if(memberRepository.findByEmail(infor)!=null) {
				Member mem=memberRepository.findByEmail(infor);
				return ResponseEntity.ok(mem);
			}
			// tim theo so dien thoai
			else if(memberRepository.findByPhone(infor)!=null) {
				Member mem=memberRepository.findByPhone(infor);
				return ResponseEntity.ok(mem);
			}
			else {
				return ResponseEntity.notFound().build();
			}
		}
		catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
		
	}
	// nhan thong tin nguoi dung
	@GetMapping("/getInfor")
	public ResponseEntity<Member> getinfor(){
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member mem=memberRepository.findByUsername(username);
			return ResponseEntity.ok(mem);
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/getAvt")
	public String getAvt() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member mem=memberRepository.findByUsername(username);
			byte[] byteArr=mem.getAvatar();
			return Base64.getEncoder().encodeToString(byteArr);
		}
		catch (Exception e) {
			return e.toString();
		}
	}
	
	
	
}
