package com.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet.entitis.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
	Card findByMemberid(int id);
	Card findByCardnumber(String number);
	
}
