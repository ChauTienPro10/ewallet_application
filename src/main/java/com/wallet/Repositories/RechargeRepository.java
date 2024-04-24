package com.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.Recharge;

public interface RechargeRepository extends JpaRepository<Recharge, Integer>{
	Recharge findByTransactioncode(String code);
}
