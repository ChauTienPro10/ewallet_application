package com.wallet.Controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wallet.Entitis.Card;
import com.wallet.Entitis.Deposit;
import com.wallet.Entitis.Member;
import com.wallet.Entitis.Transaction_block;
import com.wallet.Entitis.Transfer;
import com.wallet.Entitis.Withdrawal;
import com.wallet.Repositories.CardRepository;
import com.wallet.Repositories.DepositRepository;
import com.wallet.Repositories.MemberRepository;
import com.wallet.Repositories.TransactionRepository;
import com.wallet.Repositories.TransferRepository;
import com.wallet.Repositories.WithdrawalRepository;
import com.wallet.Utls.Feature;
import com.wallet.Utls.RandomStringExample;

@RestController
@RequestMapping("/transaction")
public class TransferControler {
	@Autowired
	CardRepository cardRepository;
	@Autowired MemberRepository memberRepository;
	
	@Autowired DepositRepository depositRepository;
	
	@Autowired WithdrawalRepository withdrawalRepository;
	
	@Autowired TransactionRepository transactionRepository;
	
	@Autowired TransferRepository transferRepository;

	
	@Autowired
	AuthenticationManager authenticationManager;
	@PostMapping("/deposit")
	public String checkBalace(@RequestBody Map<String, String> jsonData) {
		try {
			
			int id_member=Integer.parseInt(jsonData.get("member_id"));
			String password=jsonData.get("password");
			Member member=memberRepository.getById(id_member);
			if(member==null) {
				
				return "500 error";
			}
			
			
			try {
				Authentication authentication=authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(member.getUsername(),password) );
				BigDecimal amount_money=new BigDecimal(jsonData.get("amount"));
				Card card=cardRepository.findByMemberid(id_member);
				if(card == null) {
					return "The account has not yet activated the card ";
				}
				
				try {
					int stt=0;
					String preHash=null;
					
					if(transactionRepository.count()!=0) {
						stt=(int) transactionRepository.count();
						Transaction_block transaction= transactionRepository.findByBlockid(stt);
						
						preHash=transaction.getHash_block();
					}
					Deposit newDeposit=new Deposit( RandomStringExample.create_codeTrans(), id_member, amount_money, new Date(), 0, "");
					
					Transaction_block newTrans=new Transaction_block(stt+1, "", preHash, Feature.getJsonObjectDeposit(newDeposit), id_member, 1, newDeposit.getTransaction_code());
					newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));
					
					card.setBalance(card.getBalance().add(amount_money));
					cardRepository.save(card);
					depositRepository.save(newDeposit);
					transactionRepository.save(newTrans);
					return "you was deposit "+amount_money.toString()+" into your account";
				}
				catch (Exception e) {
					return e.toString();
				}
				
			}
			catch (Exception e) {
				return "Do not authenticate this account";
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	
	
	@PostMapping("/AcceptCard")
	public String OpenCard(@RequestBody Map<String, String> jsonData) {
		try {
			int id_member=Integer.parseInt(jsonData.get("member_id"));
			Random random = new Random();
	        int randomNumber = random.nextInt(899) + 101;
	         
			Member member=memberRepository.getById(id_member);
			Card card=new Card(String.valueOf(randomNumber)+member.getPhone() , id_member, jsonData
					.get("pincode"), new BigDecimal(50000));
			cardRepository.save(card);
			return "Activate card for this account success";
		}
		catch (Exception e) {
	
			e.printStackTrace();
			return e.toString();
		}
	}
	@GetMapping("/check_balance")
	public String check_balance(@RequestBody Map<String, String> jsonData) {
		try {
			int id=Integer.parseInt(jsonData.get("member_id"));
			Card card=cardRepository.findByMemberid(id);
			return card.getBalance().toString();
		}
		catch (Exception e) {
			
			return e.toString();
		}
	}
	
	
	@PostMapping("/withdrawal")
	public String withdrawal(@RequestBody Map<String, String> jsonData) {
		try {
			int id=Integer.parseInt(jsonData.get("member_id"));
			Member member=memberRepository.getById(id);
		
			try {
				Authentication authentication=authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(member.getUsername(),jsonData.get("password")) );
				BigDecimal amount_money=new BigDecimal(jsonData.get("amount"));
				Card card=cardRepository.findByMemberid(id);
				if(card == null) {
					return "The account has not yet activated the card ";
				}
				if(amount_money.compareTo(card.getBalance())>0) {
					return "balance is not enough";
				}
				int stt=0;
				String preHash=null;
				if(transactionRepository.count()!=0) {
					stt=(int) transactionRepository.count();
					Transaction_block transaction= transactionRepository.findByBlockid(stt);
					preHash=transaction.getHash_block();
				}
				Withdrawal withdrawal=new Withdrawal(RandomStringExample.create_codeTrans(),id , amount_money, new Date(), 2, "");
				Transaction_block newTrans=new Transaction_block(stt+1, "", preHash, Feature.getJsonObjectWithdraw(withdrawal), id, 2, withdrawal.getTransaction_code());
				newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));
				card.setBalance(card.getBalance().subtract(amount_money));
				withdrawalRepository.save(withdrawal);
				transactionRepository.save(newTrans);
				cardRepository.save(card);
				return "you was withdrawal "+amount_money.toString()+" out your account";
			}
			catch (Exception e) {
				return "Do not authenticate this account";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	
	@PostMapping("/transfer")
	public String transfer(@RequestBody Map<String,String>jsonData) {
		try {
			String username=jsonData.get("username");
			String password=jsonData.get("password");
			
			Member member=memberRepository.findByUsername(username);
			if(member==null) {
				return "Didn't find user";
			}
			Card sender=cardRepository.findByMemberid(member.getMember_id());
			if(sender==null) {
				return "you have not yet active this account";
			}
			Card receiver=cardRepository.findByCardnumber(jsonData.get("cardnumber"));
			if(receiver==null) {
				return "Didn't find address of receiver";
			}
			try {
				Authentication authentication=authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(member.getUsername(),password) );
				BigDecimal amount_money=new BigDecimal(jsonData.get("amount"));
				if(amount_money.compareTo(sender.getBalance())>0) {
					return "balance is not enough";
				}
				int stt=0;
				String preHash=null;
				if(transactionRepository.count()!=0) {
					stt=(int) transactionRepository.count();
					Transaction_block transaction= transactionRepository.findByBlockid(stt);
					preHash=transaction.getHash_block();
				}
				Transfer transfer=new Transfer(RandomStringExample.create_codeTrans(), member.getMember_id(), receiver.getMember_id(), amount_money, new Date(), 0, jsonData.get("note"));
				Transaction_block newTrans=new Transaction_block(stt+1, "", preHash, Feature.getJsonObjectTrasfer(transfer), member.getMember_id(), 3, transfer.getTransaction_code());
				newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));
				sender.setBalance(sender.getBalance().subtract(amount_money));
				receiver.setBalance(receiver.getBalance().add(amount_money));
				transferRepository.save(transfer);
				transactionRepository.save(newTrans);
				cardRepository.save(sender);
				cardRepository.save(receiver);
				return "you transfered money success";
				
			}
			catch (Exception e) {
				return e.toString();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

}