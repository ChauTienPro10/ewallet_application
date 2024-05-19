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
	@PostMapping("/transfer")
	public String transfer(@RequestBody Map<String, String> jsonData) {
		try {
			String toAddress=jsonData.get("receiver");
			double amount=Double.parseDouble(jsonData.get("amount"));
			amount=amount*Math.pow(10, 18);
			System.out.print(BigDecimal.valueOf(amount).toBigInteger());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member=memberRepository.findByUsername(username);
			EtherWallet etherWallet=ethereumRepository.findByMemberid(member.getMember_id());
			if(etherWallet==null) {
				return "user have not yet link to ETH";
			}
			String password=jsonData.get("pin");
			authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			String key=DecryptionExample.decryp(etherWallet.getKey(), password);
			System.out.print(key);
			MyContractWrapper myContractWrapper=new MyContractWrapper(key);
			if (myContractWrapper.isPrivateKeyValid(key, etherWallet.getAddress())==true) {
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
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member=memberRepository.findByUsername(username);
			BigDecimal eth=MyContractWrapper.convertVndToEther(new BigDecimal(jsonData.get("amount").replaceAll(",", "")));
			double amount=eth.doubleValue();
			amount=amount*Math.pow(10, 18);
			
			String toAddress="0x09283041db45446836D18180a61943fBeA403C72";
			EtherWallet etherWallet=ethereumRepository.findByMemberid(member.getMember_id());
			if(etherWallet==null) {
				return "user have not yet link to ETH";
			}
			String password=jsonData.get("pin");
			String key=DecryptionExample.decryp(etherWallet.getKey(), password);
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
				Deposit.doTransfer(member.getMember_id(),new BigDecimal(jsonData.get("amount").replaceAll(",", "")), stt, preHash, card, cardRepository, depositRepository, transactionRepository);
	
				return "OK";
			}
			return "NO";
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}
	@PostMapping("/withdraw")
	public String withdraw(@RequestBody Map<String, String>jsonData) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member=memberRepository.findByUsername(username);
			EtherWallet etherWallet=ethereumRepository.findByMemberid(member.getMember_id());
			BigDecimal eth=MyContractWrapper.convertVndToEther(new BigDecimal(jsonData.get("amount").replaceAll(",", "")));
			double amount=eth.doubleValue();
			amount=amount*Math.pow(10, 18);
			if(etherWallet==null) {
				return "user have not yet link to ETH";
			}
			Member memberAdmin=memberRepository.findByMemberid(9);
			EtherWallet etherWalletAdmin=ethereumRepository.findByMemberid(9);
			String passwordadmin="112233";
			String keyadmin=DecryptionExample.decryp(etherWalletAdmin.getKey(), passwordadmin);
			
			String password=jsonData.get("pin");
			String key=DecryptionExample.decryp(etherWallet.getKey(), password);
			MyContractWrapper myContractWrapperAd=new MyContractWrapper(keyadmin);
		
			if (myContractWrapperAd.isPrivateKeyValid(key, etherWallet.getAddress())==true) {
				try {
					RemoteFunctionCall<TransactionReceipt> remoteCall 
					= myContractWrapperAd.transferETH(etherWallet.getAddress(),BigDecimal.valueOf(amount).toBigInteger());
					TransactionReceipt transactionReceipt = remoteCall.send();

					Withdrawal.dowithdraw(member, cardRepository, new BigDecimal(jsonData.get("amount").replaceAll(",", "")), transactionRepository, withdrawalRepository);
					return "OK";
				}
				catch (Exception e) {
					e.printStackTrace();
					return "NO";
				}
				
			}
			return "NO";
		}
		catch (Exception e) {
			return e.toString();
		}
	}
	
	@GetMapping("/getEth")
	public ResponseEntity<EtherWallet> getInfWallet(){
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member=memberRepository.findByUsername(username);
			EtherWallet eth=ethereumRepository.findByMemberid(member.getMember_id());
			if(eth!=null) {
				return ResponseEntity.ok(eth);
			}
			else return ResponseEntity.notFound().build();
		}
		catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
		
	}
	@PostMapping("/getEthBalance")
	public String getbalance(@RequestBody Map<String, String> jsonData) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member=memberRepository.findByUsername(username);
			EtherWallet eth=ethereumRepository.findByMemberid(member.getMember_id());
			String pass=jsonData.get("pin");
			String key=DecryptionExample.decryp(eth.getKey(), pass);
			MyContractWrapper myContractWrapperAd=new MyContractWrapper(key);
			System.out.println(eth.getAddress());
	
			return myContractWrapperAd.getAccountBalance(eth.getAddress()).toString().substring(0,4);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
	}

}
