package com.wallet.entitis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_etherwallet")
public class EtherWallet {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id ;
	
	@Column(name="member_id")
	private int memberid;
	
	@Column(name="address")
	private String address;
	
	@Column(name="pri_key")
	private String key;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public EtherWallet(int memberid, String address, String key) {
		super();
		this.memberid = memberid;
		this.address = address;
		this.key = key;
	}

	public EtherWallet() {
		super();
	}
	
	

}
