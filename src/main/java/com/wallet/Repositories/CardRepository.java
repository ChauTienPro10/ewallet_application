package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.Card;

public interface CardRepository extends JpaRepository<Card, Integer> {
	Card findByMemberid(int id);
	Card findByCardnumber(String number);
	
}
