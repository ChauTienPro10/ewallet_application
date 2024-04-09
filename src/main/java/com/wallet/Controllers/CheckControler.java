package com.wallet.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.Entitis.Deposit;
import com.wallet.Entitis.History_tranfer;
import com.wallet.Entitis.Member;
import com.wallet.Entitis.Transaction_block;
import com.wallet.Entitis.Transfer;
import com.wallet.Entitis.Withdrawal;
import com.wallet.Repositories.DepositRepository;
import com.wallet.Repositories.MemberRepository;
import com.wallet.Repositories.TransactionRepository;
import com.wallet.Repositories.TransferRepository;
import com.wallet.Repositories.WithdrawalRepository;
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
	
	@GetMapping("/history")
	public java.util.List<History_tranfer> getAllTransactions(@RequestBody Map<String,String>jsonData){

		List<History_tranfer> historis=new ArrayList<History_tranfer>();
		try {
			List<Transaction_block> trans=transactionRepository.findByMemberid(Integer.parseInt(jsonData.get("member_id")));
			if(trans.size()>0) {
				for (int i = 0; i < trans.size(); i++) {
				    History_tranfer ht=new History_tranfer();
//				    System.out.println(trans.get(i).getTransaction_code());
				    if(trans.get(i).getTransaction_type()==1) {
				    	Deposit dep=depositRepository.findByTransactioncode(trans.get(i).getTransaction_code());
				    	if(dep!=null) {
				    		ht.setType("deposit");					    	
					    	ht.setTime(dep.getDate_time());
					    	ht.setAmount(dep.getDeposit_amount());
					    	ht.setContent("You deposited "+dep.getDeposit_amount()+ " into your account");
				    	}
				    	
				    }
				    else if(trans.get(i).getTransaction_type()==2) {
				    	Withdrawal withdraw=withdrawalRepository.findByTransactioncode(trans.get(i).getTransaction_code());
				    	if(withdraw!=null) {
				    		ht.setType("withdraw");
					    	
					    	ht.setTime(withdraw.getDate_time());
					    	ht.setAmount(withdraw.getAmount());
					    	ht.setContent("You withdrawed "+withdraw.getAmount()+ " out your account");
				    	}
				    	
				    }else if(trans.get(i).getTransaction_type()==3) {
				    	Transfer tran=transferRepository.findByTransactioncode(trans.get(i).getTransaction_code());
				    	if(tran!=null) {
				    		ht.setType("transfer");
					    	
					    	Member mem=memberRepository.findByMemberid(tran.getReceive_id());
					    	ht.setReceiver(mem.getFname()+" "+mem.getLname());
					    	ht.setTime(tran.getDate_time());
					    	ht.setContent(tran.getNote());
					    	ht.setAmount(tran.getTransfer_amount());
				    	}
				    	
				    }else {
				    	break;
				    }
				    historis.add(ht);
				    
				}
				return historis;
			}
			else return null;
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
		
		
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
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
}
