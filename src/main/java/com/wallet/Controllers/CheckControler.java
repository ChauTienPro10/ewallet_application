package com.wallet.controllers;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/history")
	public List<History_tranfer> getAllTransactions(@RequestBody Map<String, String> jsonData) {
	    List<History_tranfer> historis = new ArrayList<>();
	    try {
	        int member_id = Integer.parseInt(jsonData.get("member_id"));
	        List<Transaction_block> trans = transactionRepository.findByMemberid(member_id);

	        for (Transaction_block transaction : trans) {
	            History_tranfer ht = createHistoryTransfer(transaction);
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
	private History_tranfer createHistoryTransfer(Transaction_block transaction) {
	    History_tranfer ht = new History_tranfer();
	    int transactionType = transaction.getTransaction_type();
	    String transactionCode = transaction.getTransaction_code();

	    switch (transactionType) {
	        case 1:
	            Deposit dep = depositRepository.findByTransactioncode(transactionCode);
	            if (dep != null) {
	                ht.setType("deposit");
	                ht.setTime(dep.getDate_time());
	                ht.setAmount(dep.getDeposit_amount());
	                ht.setContent("You deposited " + dep.getDeposit_amount() + " into your account");
	                return ht;
	            }
	            break;

	        case 2:
	            Withdrawal withdraw = withdrawalRepository.findByTransactioncode(transactionCode);
	            if (withdraw != null) {
	                ht.setType("withdraw");
	                ht.setTime(withdraw.getDate_time());
	                ht.setAmount(withdraw.getAmount());
	                ht.setContent("You withdrew " + withdraw.getAmount() + " from your account");
	                return ht;
	            }
	            break;

	        case 3:
	            Transfer tran = transferRepository.findByTransactioncode(transactionCode);
	            if (tran != null) {
	                ht.setType("transfer");
	                Member mem = memberRepository.findByMemberid(tran.getReceive_id());
	                ht.setReceiver(mem.getFname() + " " + mem.getLname());
	                ht.setTime(tran.getDate_time());
	                ht.setContent(tran.getNote());
	                ht.setAmount(tran.getTransfer_amount());
	                return ht;
	            }
	            break;

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

	
	
	@GetMapping("/findmember")
	public ResponseEntity<Member> findmember(@RequestBody Map<String, String> jsonData){
		try {
			String infor=jsonData.get("infor_data");
			
			if(memberRepository.findByUsername(infor)!=null) {
				Member mem=memberRepository.findByUsername(infor);
				return ResponseEntity.ok(mem);
			}
			else if(memberRepository.findByEmail(infor)!=null) {
				Member mem=memberRepository.findByEmail(infor);
				return ResponseEntity.ok(mem);
			}
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
