package com.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {
	Transfer findByTransactioncode(String code);

}
