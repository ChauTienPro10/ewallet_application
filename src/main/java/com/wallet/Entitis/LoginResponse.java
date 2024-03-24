package com.wallet.Entitis;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;

public class LoginResponse {
    private String jwt;
    private List<GrantedAuthority> Role;
    private String username;
    private long id;
    
    
    
    

    public LoginResponse(String jwt, List<GrantedAuthority> role, String username, long id) {
		super();
		this.jwt = jwt;
		Role = role;
		this.username = username;
		this.id = id;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	

	public List<GrantedAuthority> getRole() {
		return Role;
	}


	public void setRole(List<GrantedAuthority> role) {
		Role = role;
	}


	


    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
