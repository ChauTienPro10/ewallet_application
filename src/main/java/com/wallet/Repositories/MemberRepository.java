package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {
	
	Member findByEmail(String mail);
	Member findByUsername(String username);
	Member findByUsernameAndPassword(String username, String password);
	Member findByMemberid(int id);
	Member findByPhone(String phone);
	
	

}
