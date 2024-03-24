package com.wallet.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
	User findByUsernameAndPassword(String username, String password);
	User findByEmail(String email);
}
