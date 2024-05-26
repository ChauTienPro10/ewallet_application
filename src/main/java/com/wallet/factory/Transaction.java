package com.wallet.factory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.History_tranfer;
import com.wallet.entitis.Transaction_block;

public interface Transaction {
	void get_repository(JpaRepository repo,JpaRepository member);
	History_tranfer setHistory_transaction(Transaction_block transaction);
	
}
