package com.wallet.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.History_tranfer;
import com.wallet.entitis.Transaction_block;
import com.wallet.entitis.Withdrawal;
import com.wallet.repositories.WithdrawalRepository;

public class WithdrawImpl implements Transaction {
	@Autowired WithdrawalRepository withdrawalRepository;
	@Override
	public History_tranfer setHistory_transaction(Transaction_block transaction) {
		History_tranfer ht = new History_tranfer();
		Withdrawal withdraw = withdrawalRepository.findByTransactioncode(transaction.getTransaction_code());
		  ht.setType("withdraw");
          ht.setTime(withdraw.getDate_time());
          ht.setAmount(withdraw.getAmount());
          ht.setContent("You withdraw " + withdraw.getAmount() + " from your account");
          return ht;
	}
	@Override
	public void get_repository(JpaRepository repo , JpaRepository member) {
		this.withdrawalRepository=(WithdrawalRepository) repo;
	}

}
