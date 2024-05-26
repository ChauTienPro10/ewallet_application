package com.wallet.state;

import java.math.BigDecimal;

import com.wallet.entitis.Card;
import com.wallet.entitis.Member;
import com.wallet.repositories.CardRepository;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.WithdrawalRepository;

public interface CardState {
	
	boolean dowithdraw(Member member,CardRepository cardRepository,BigDecimal amount_money,TransactionRepository transactionRepository
			, WithdrawalRepository withdrawalRepository);
	void doTransfer(int id_member,BigDecimal amount_money,int stt,String preHash,Card card,CardRepository cardRepository,
			DepositRepository depositRepository,TransactionRepository transactionRepository);
}
