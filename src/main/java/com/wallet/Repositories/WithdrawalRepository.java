package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.Withdrawal;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Integer> {

}
