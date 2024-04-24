package com.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {
	Deposit findByTransactioncode(String code);
	
}
