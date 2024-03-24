package com.wallet.Entitis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="tbl_transaction_block")
public class Transaction_block {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="block_id")
	private int block_id;
	
	@Column(name="hash_block")
	private String hash_block;
	
	@Column(name="pre_hash_block")
	private String pre_hash_block;
	
	@Column(name="transaction_data")
	private String transaction_data;
	
	@Column(name="member_id")
	private int member_id;
	
	@Column(name="transaction_type")
	private int transaction_type;
	
	@Column(name="transaction_code")
	private String transaction_code;

	public int getBlock_id() {
		return block_id;
	}

	public void setBlock_id(int block_id) {
		this.block_id = block_id;
	}

	public String getHash_block() {
		return hash_block;
	}

	public void setHash_block(String hash_block) {
		this.hash_block = hash_block;
	}

	public String getPre_hash_block() {
		return pre_hash_block;
	}

	public void setPre_hash_block(String pre_hash_block) {
		this.pre_hash_block = pre_hash_block;
	}

	public String getTransaction_data() {
		return transaction_data;
	}

	public void setTransaction_data(String transaction_data) {
		this.transaction_data = transaction_data;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public int getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(int transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getTransaction_code() {
		return transaction_code;
	}

	public void setTransaction_code(String transaction_code) {
		this.transaction_code = transaction_code;
	}

	public Transaction_block(String hash_block, String pre_hash_block, String transaction_data, int member_id,
			int transaction_type, String transaction_code) {
		super();
		this.hash_block = hash_block;
		this.pre_hash_block = pre_hash_block;
		this.transaction_data = transaction_data;
		this.member_id = member_id;
		this.transaction_type = transaction_type;
		this.transaction_code = transaction_code;
	}

	public Transaction_block() {
		super();
	}
	
	
}
