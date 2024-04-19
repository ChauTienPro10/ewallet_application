package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.Recharge;

public interface RechargeRepository extends JpaRepository<Recharge, Integer>{
	Recharge findByTransactioncode(String code);
}
