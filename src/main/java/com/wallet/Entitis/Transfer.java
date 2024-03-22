package com.wallet.Entitis;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_transfer")
public class Transfer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="transfer_id")
	private int transfer_id;
	
	@Column(name="transaction_code")
	private String transaction_code;
	
	@Column(name="member_id")
	private int member_id;
	
	@Column(name="receive_id")
	private int receive_id;
	
	@Column(name="transfer_amount")
	private BigDecimal transfer_amount;
	
	@Column(name="date_time")
	private Date date_time;
	
	@Column(name="status")
	private int status;
	
	@Column(name="note")
	private String note;

	public int getTransfer_id() {
		return transfer_id;
	}

	public void setTransfer_id(int transfer_id) {
		this.transfer_id = transfer_id;
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

	public int getReceive_id() {
		return receive_id;
	}

	public void setReceive_id(int receive_id) {
		this.receive_id = receive_id;
	}

	public BigDecimal getTransfer_amount() {
		return transfer_amount;
	}

	public void setTransfer_amount(BigDecimal transfer_amount) {
		this.transfer_amount = transfer_amount;
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

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Transfer(String transaction_code, int member_id, int receive_id, BigDecimal transfer_amount, Date date_time,
			int status, String note) {
		super();
		this.transaction_code = transaction_code;
		this.member_id = member_id;
		this.receive_id = receive_id;
		this.transfer_amount = transfer_amount;
		this.date_time = date_time;
		this.status = status;
		this.note = note;
	}

	public Transfer() {
		super();
	}
	

}
