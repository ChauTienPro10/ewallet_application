package com.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.wallet.entitis.EtherWallet;

@Repository
public interface EthereumRepository extends JpaRepository<EtherWallet, Integer>{
	EtherWallet findByAddress(String add);
	EtherWallet findByMemberid(int id);
}
