package com.wallet.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.wallet.Entitis.Transaction_block;

public interface TransactionRepository extends JpaRepository<Transaction_block, Integer> {
	 Transaction_block findByBlockid(int id);
	 List<Transaction_block> findByMemberid(int id);
}
