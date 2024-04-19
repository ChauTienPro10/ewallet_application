package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet.Entitis.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
	Card findByMemberid(int id);
	Card findByCardnumber(String number);
	
}
