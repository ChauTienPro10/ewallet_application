package com.wallet.state;

import java.math.BigDecimal;
import java.util.Date;

import com.wallet.entitis.Card;
import com.wallet.entitis.Deposit;
import com.wallet.entitis.Member;
import com.wallet.entitis.Transaction_block;
import com.wallet.entitis.Withdrawal;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.WithdrawalRepository;
import com.wallet.utls.Feature;
import com.wallet.utls.RandomStringExample;

public class ActingState implements CardState {

	@Override
	public boolean dowithdraw(Member member, CardRepository cardRepository, BigDecimal amount_money,
			TransactionRepository transactionRepository, WithdrawalRepository withdrawalRepository) {
		Card card = cardRepository.findByMemberid(member.getMember_id());
		if (card == null) {
			return false;
		}
		if (amount_money.compareTo(card.getBalance()) > 0) {
			return false;
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
		return true;
	}

	@Override
	public void doTransfer(int id_member, BigDecimal amount_money, int stt, String preHash, Card card,
			CardRepository cardRepository, DepositRepository depositRepository,
			TransactionRepository transactionRepository) {
		Deposit newDeposit = new Deposit(RandomStringExample.create_codeTrans(), id_member, amount_money,
				new Date(), 0, "");

		Transaction_block newTrans = new Transaction_block(stt + 1, "", preHash,
				Feature.getJsonObjectDeposit(newDeposit), id_member, 1, newDeposit.getTransaction_code());
		newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));

		card.setBalance(card.getBalance().add(amount_money));
		cardRepository.save(card);
		depositRepository.save(newDeposit);
		transactionRepository.save(newTrans);
		
	}

	

}
