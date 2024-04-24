package com.wallet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
	User findByEmail(String email);
}
