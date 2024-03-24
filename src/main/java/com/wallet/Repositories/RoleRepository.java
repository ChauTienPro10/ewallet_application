package com.wallet.Repositories;



import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.Entitis.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {
	
}
