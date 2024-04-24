package com.wallet.entitis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="user_id")
	private int user_id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="complete_name")
	private String fullname;
	
	@Column(name="email_address")
	private String email;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User(String username, String password, String fullname, String email) {
		super();
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
	}

	public User() {
		super();
	}
	
	
}
