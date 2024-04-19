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
@Table(name="tbl_recharge")
public class Recharge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="recharge_id")
	private int rechargeid;
	
	@Column(name="transaction_code")
	private String transactioncode;
	
	@Column(name="member_id")
	private int memberid;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="date_time")
	private Date time;
	
	@Column(name="status")
	private int status;
	
	@Column(name="comments")
	private String note;

	public int getRechargeid() {
		return rechargeid;
	}

	public void setRechargeid(int rechargeid) {
		this.rechargeid = rechargeid;
	}

	public String getTransactioncode() {
		return transactioncode;
	}

	public void setTransactioncode(String transactioncode) {
		this.transactioncode = transactioncode;
	}

	public int getMemberid() {
		return memberid;
	}

	public void setMemberid(int memberid) {
		this.memberid = memberid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Recharge(String transactioncode, int memberid, String phone, BigDecimal amount, Date time, int status,
			String note) {
		super();
		this.transactioncode = transactioncode;
		this.memberid = memberid;
		this.phone = phone;
		this.amount = amount;
		this.time = time;
		this.status = status;
		this.note = note;
	}
	public Recharge() {
		super();
	
	}
	
}
