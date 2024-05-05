package com.wallet.controllers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.wallet.entitis.Card;
import com.wallet.entitis.Deposit;
import com.wallet.entitis.Member;
import com.wallet.entitis.Recharge;
import com.wallet.entitis.Transaction_block;
import com.wallet.entitis.Transfer;

import com.wallet.entitis.Withdrawal;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.MemberRepository;
import com.wallet.repositories.RechargeRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.TransferRepository;
import com.wallet.repositories.WithdrawalRepository;
import com.wallet.utls.Feature;
import com.wallet.utls.MyQr;
import com.wallet.utls.RandomStringExample;

@RestController
@RequestMapping("/transaction")
public class TransferControler {
	@Autowired
	CardRepository cardRepository;
	@Autowired
	MemberRepository memberRepository;

	@Autowired
	DepositRepository depositRepository;

	@Autowired
	WithdrawalRepository withdrawalRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Autowired
	TransferRepository transferRepository;
	@Autowired
	RechargeRepository rechargeRepository;

	@Autowired
	AuthenticationManager authenticationManager;

	@PostMapping("/deposit")
	public String checkBalace(@RequestBody Map<String, String> jsonData) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member = memberRepository.findByUsername(username);

			int id_member = member.getMember_id();

			BigDecimal amount_money = new BigDecimal(jsonData.get("amount").replaceAll(",", ""));
			Card card = cardRepository.findByMemberid(id_member);
			if (card == null) {
				return "The account has not yet activated the card ";
			}

			try {
				int stt = 0;
				String preHash = null;

				if (transactionRepository.count() != 0) {
					stt = (int) transactionRepository.count();
					Transaction_block transaction = transactionRepository.findByBlockid(stt);

					preHash = transaction.getHash_block();
				}
				Deposit newDeposit = new Deposit(RandomStringExample.create_codeTrans(), id_member, amount_money,
						new Date(), 0, "");

				Transaction_block newTrans = new Transaction_block(stt + 1, "", preHash,
						Feature.getJsonObjectDeposit(newDeposit), id_member, 1, newDeposit.getTransaction_code());
				newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));

				card.setBalance(card.getBalance().add(amount_money));
				cardRepository.save(card);
				depositRepository.save(newDeposit);
				transactionRepository.save(newTrans);
				return "you was deposit " + amount_money.toString() + " into your account";

			} catch (Exception e) {
				return "Do not authenticate this account";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@PostMapping("/AcceptCard")
	public String OpenCard(@RequestBody Map<String, String> jsonData) {
		try {
			int id_member = Integer.parseInt(jsonData.get("member_id"));
			Random random = new Random();
			int randomNumber = random.nextInt(899) + 101;

			Member member = memberRepository.getById(id_member);
			Card card = new Card(String.valueOf(randomNumber) + member.getPhone(), id_member, jsonData.get("pincode"),
					new BigDecimal(50000));
			cardRepository.save(card);
			return "Activate card for this account success";
		} catch (Exception e) {

			e.printStackTrace();
			return e.toString();
		}
	}

	@GetMapping("/check_balance")
	public String check_balance() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member = memberRepository.findByUsername(username);
			int id = member.getMember_id();
			Card card = cardRepository.findByMemberid(id);
			return card.getBalance().toString();
		} catch (Exception e) {

			return e.toString();
		}
	}

	@PostMapping("/withdrawal")
	public String withdrawal(@RequestBody Map<String, String> jsonData) {
		try {

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member member = memberRepository.findByUsername(username);

			BigDecimal amount_money = new BigDecimal(jsonData.get("amount").replaceAll(",", ""));
			Card card = cardRepository.findByMemberid(member.getMember_id());
			if (card == null) {
				return "The account has not yet activated the card ";
			}
			if (amount_money.compareTo(card.getBalance()) > 0) {
				return "balance is not enough";
			}
			int stt = 0;
			String preHash = null;
			if (transactionRepository.count() != 0) {
				stt = (int) transactionRepository.count();
				Transaction_block transaction = transactionRepository.findByBlockid(stt);
				preHash = transaction.getHash_block();
			}
			Withdrawal withdrawal = new Withdrawal(RandomStringExample.create_codeTrans(), member.getMember_id(), amount_money, new Date(),
					2, "");
			Transaction_block newTrans = new Transaction_block(stt + 1, "", preHash,
					Feature.getJsonObjectWithdraw(withdrawal), member.getMember_id(), 2, withdrawal.getTransaction_code());
			newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));
			card.setBalance(card.getBalance().subtract(amount_money));
			withdrawalRepository.save(withdrawal);
			transactionRepository.save(newTrans);
			cardRepository.save(card);
			return "you was withdrawal " + amount_money.toString() + " out your account";

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@PostMapping("/transfer")
	public String transfer(@RequestBody Map<String, String> jsonData) {
		try {
			String username = jsonData.get("username");
			String password = jsonData.get("password");

			Member member = memberRepository.findByUsername(username);
			if (member == null) {
				return "Didn't find user";
			}
			Card sender = cardRepository.findByMemberid(member.getMember_id());
			if (sender == null) {
				return "you have not yet active this account";
			}
			Card receiver = cardRepository.findByCardnumber(jsonData.get("cardnumber"));
			if (receiver == null) {
				return "Didn't find address of receiver";
			}
			try {
				@SuppressWarnings("unused")
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(member.getUsername(), password));
				BigDecimal amount_money = new BigDecimal(jsonData.get("amount"));
				if (amount_money.compareTo(sender.getBalance()) > 0) {
					return "balance is not enough";
				}
				int stt = 0;
				String preHash = null;
				if (transactionRepository.count() != 0) {
					stt = (int) transactionRepository.count();
					Transaction_block transaction = transactionRepository.findByBlockid(stt);
					preHash = transaction.getHash_block();
				}
				Transfer transfer = new Transfer(RandomStringExample.create_codeTrans(), member.getMember_id(),
						receiver.getMember_id(), amount_money, new Date(), 0, jsonData.get("note"));
				Transaction_block newTrans = new Transaction_block(stt + 1, "", preHash,
						Feature.getJsonObjectTrasfer(transfer), member.getMember_id(), 3,
						transfer.getTransaction_code());
				newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));
				sender.setBalance(sender.getBalance().subtract(amount_money));
				receiver.setBalance(receiver.getBalance().add(amount_money));
				transferRepository.save(transfer);
				transactionRepository.save(newTrans);
				cardRepository.save(sender);
				cardRepository.save(receiver);
				return "you transfered money success";

			} catch (Exception e) {
				return e.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}
	}

	@PostMapping("/getQRData")
	public String getQRdata(@RequestBody String requestBody) {
		System.out.println("Request Body: " + requestBody);
		return requestBody;
	}

	@PostMapping("/createQR")
	public String createQR(@RequestBody Map<String, String> jsonData) {
		try {

			BigDecimal amount = new BigDecimal(jsonData.get("amount"));
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			Member us = memberRepository.findByUsername(username);
			Card card = cardRepository.findByMemberid(us.getMember_id());
			String data = us.getFname() + " " + us.getLname() + "\n" + card.getCard_number() + "\n" + amount.toString();
			Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			String charset = "UTF-8";
			hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			return MyQr.createQR(data, charset, hashMap, 200, 200);

		} catch (Exception e) {

			e.printStackTrace();
			return "error while create QR code!";
		}
	}

	@PostMapping("/readQR")
	public String readQR(@RequestBody Map<String, String> jsonData) {
		try {
			String data = jsonData.get("base64");
			return MyQr.readQR(MyQr.convertBase64ToImage(data), "UTF-8");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return "error!";
		}
	}

	@PostMapping("/recharge_phone")
	public String recharge_phone(@RequestBody Map<String, String> jsonData) {

		try {
			int idmember = Integer.parseInt(jsonData.get("member_id"));
			String phonne = jsonData.get("phone");
			if (Feature.validatePhoneNumber(phonne) == false) {
				return "this phone number is invalid!";
			}
			BigDecimal amount = new BigDecimal(jsonData.get("amount"));
			if ((amount.remainder(BigDecimal.valueOf(10000))).compareTo(BigDecimal.valueOf(0)) != 0) {
				return "amount is invalid";
			}

			if (memberRepository.findByMemberid(idmember) == null) {
				return "this member don't exist";
			}
			Member member = memberRepository.findByPhone(phonne);
			Card card = cardRepository.findByMemberid(member.getMember_id());
			if (card == null) {
				return "this phone number have not yet active card!";
			}
			if (amount.compareTo(card.getBalance()) > 0) {
				return "balance is not enough";
			}
			int stt = 0;
			String preHash = null;
			if (transactionRepository.count() != 0) {
				stt = (int) transactionRepository.count();
				Transaction_block transaction = transactionRepository.findByBlockid(stt);
				preHash = transaction.getHash_block();
			}
			Recharge recharge = new Recharge(RandomStringExample.create_codeTrans(), member.getMember_id(), phonne,
					amount, new Date(), 0, "recharge to " + phonne);
			Transaction_block newRecharge = new Transaction_block(stt + 1, "", preHash,
					Feature.getJsonObjectRecharge(recharge), member.getMember_id(), 4, recharge.getTransactioncode());
			// 4 recharge
			newRecharge.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectRecharge(recharge)));
			card.setBalance(card.getBalance().subtract(amount));
			transactionRepository.save(newRecharge);
			rechargeRepository.save(recharge);
			cardRepository.save(card);
			return "you recharged " + amount + " to this phone";
		} catch (Exception e) {
			e.printStackTrace();
			return "Can't recharge to this phone!";
		}
	}

}
