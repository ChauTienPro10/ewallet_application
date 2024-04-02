package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.Deposit;

public interface DepositRepository extends JpaRepository<Deposit, Integer> {
	Deposit findByTransactioncode(String code);
	
}
