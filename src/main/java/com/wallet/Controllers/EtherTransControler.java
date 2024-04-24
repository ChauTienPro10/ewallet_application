package com.wallet.controllers;

import java.math.BigDecimal;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.wallet.DecryptionExample;

import com.wallet.entitis.EtherWallet;
import com.wallet.entitis.Member;
import com.wallet.repositories.EthereumRepository;
import com.wallet.repositories.MemberRepository;
import com.wallet.web3j.MyContractWrapper;

@RestController
@RequestMapping("/ETH")
public class EtherTransControler {
	@Autowired MemberRepository memberRepository;
	@Autowired EthereumRepository ethereumRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	
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
			String password=jsonData.get("password");
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

}
