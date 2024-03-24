package com.wallet.Entitis;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="tbl_withdrawal")
public class Withdrawal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="withdrawal_id")
	private int withdrawal_id;
	
	@Column(name="transaction_code")
	private String transaction_code;
	
	@Column(name="member_id")
	private int member_id;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="date_time")
	private Date date_time;
	
	@Column(name="status")
	private int status;
	
	@Column(name="comments")
	private String comments;

	public int getWithdrawal_id() {
		return withdrawal_id;
	}

	public void setWithdrawal_id(int withdrawal_id) {
		this.withdrawal_id = withdrawal_id;
	}

	public String getTransaction_code() {
		return transaction_code;
	}

	public void setTransaction_code(String transaction_code) {
		this.transaction_code = transaction_code;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Withdrawal(String transaction_code, int member_id, BigDecimal amount, Date date_time, int status,
			String comments) {
		super();
		this.transaction_code = transaction_code;
		this.member_id = member_id;
		this.amount = amount;
		this.date_time = date_time;
		this.status = status;
		this.comments = comments;
	}

	public Withdrawal() {
		super();
	}
	
	
}
