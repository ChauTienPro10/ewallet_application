package com.wallet.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.Deposit;
import com.wallet.entitis.History_tranfer;
import com.wallet.entitis.Transaction_block;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.MemberRepository;

public class DepositImpl implements Transaction {
	@Autowired
	DepositRepository depositRepository;
	@Autowired MemberRepository memberRepository;

	@Override
	public History_tranfer setHistory_transaction(Transaction_block transaction) {
		History_tranfer ht = new History_tranfer();

		Deposit dep = depositRepository.findByTransactioncode(transaction.getTransaction_code());
		ht.setType("deposit");
		ht.setTime(dep.getDate_time());
		ht.setAmount(dep.getDeposit_amount());
		ht.setContent("You deposited " + dep.getDeposit_amount() + " into your account");
		return ht;

	}

	@Override
	public void get_repository(JpaRepository repo,JpaRepository member) {
		this.depositRepository=(DepositRepository) repo;
	
	}

}
