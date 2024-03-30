package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {

}
