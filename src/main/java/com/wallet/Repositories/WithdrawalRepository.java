package com.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.Withdrawal;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer> {
	Withdrawal findByTransactioncode(String code);

}
