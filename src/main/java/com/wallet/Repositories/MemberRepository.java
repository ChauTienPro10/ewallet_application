package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet.Entitis.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	Member findByEmail(String mail);
	Member findByUsername(String username);
	Member findByUsernameAndPassword(String username, String password);
	Member findByMemberid(int id);
	Member findByPhone(String phone);
	
	

}
