package com.wallet.entitis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wallet.state.ActingState;
import com.wallet.state.BlockState;
import com.wallet.state.CardState;

@Entity
@Table(name="tbl_member")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="member_id")
	private int memberid;
	
	@Column(name="first_name")
	private String fname;
	
	@Column(name="last_name")
	private String lname;
	
	@Column(name="email_address")
	private String email;
	
	@Column(name="country")
	private String country;
	
	@Column(name="contact_number")
	private String phone;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private String password;
	
	@Column(name="account_status")
	int account_status;

	@Column(name="avatar")
	private byte[] avatar;
	
	public byte[] getAvatar() {
		return avatar;
	}

	public void setAvatar(byte[] avatar) {
		this.avatar = avatar;
	}

	public int getMember_id() {
		return memberid;
	}

	public void setMember_id(int member_id) {
		this.memberid = member_id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public int getAccount_status() {
		return account_status;
	}

	public void setAccount_status(int account_status) {
		this.account_status = account_status;
	}

	public Member(String fname, String lname, String email, String country, String phone, String username,
			String password, int account_status) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.country = country;
		this.phone = phone;
		this.username = username;
		this.password = password;
		this.account_status = account_status;
	}

	public Member() {
		super();
	}
	
	
	public CardState initState() {
		if (this.account_status == 0) {
			return new ActingState();
		}
		else return new BlockState();
	}
	
	
	
	
}
