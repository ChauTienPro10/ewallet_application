package com.wallet.state;

import java.math.BigDecimal;

import com.wallet.entitis.Card;
import com.wallet.entitis.Member;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.WithdrawalRepository;

public class BlockState implements CardState{

	@Override
	public boolean dowithdraw(Member member, CardRepository cardRepository, BigDecimal amount_money,
			TransactionRepository transactionRepository, WithdrawalRepository withdrawalRepository) {
		return false;
	}

	@Override
	public void doTransfer(int id_member, BigDecimal amount_money, int stt, String preHash, Card card,
			CardRepository cardRepository, DepositRepository depositRepository,
			TransactionRepository transactionRepository) {
	
		
	}

}
