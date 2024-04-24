package com.wallet.repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entitis.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {
	
}
