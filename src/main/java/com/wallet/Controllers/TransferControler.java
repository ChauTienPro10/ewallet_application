package com.wallet.Controllers;

import java.math.BigDecimal;
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
import com.wallet.Entitis.Member;
import com.wallet.Repositories.CardRepository;
import com.wallet.Repositories.DepositRepository;
import com.wallet.Repositories.MemberRepository;
import com.wallet.Repositories.WithdrawalRepository;

@RestController
@RequestMapping("/transaction")
public class TransferControler {
	@Autowired
	CardRepository cardRepository;
	@Autowired MemberRepository memberRepository;
	
	@Autowired DepositRepository depositRepository;
	
	@Autowired WithdrawalRepository withdrawalRepository;
	
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
				card.setBalance(card.getBalance().add(amount_money));
				cardRepository.save(card);
				return "you was deposit "+amount_money.toString()+" into your account";
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
				card.setBalance(card.getBalance().subtract(amount_money));
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

}
