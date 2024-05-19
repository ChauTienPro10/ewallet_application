package com.wallet.entitis;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.wallet.repositories.CardRepository;
import com.wallet.repositories.DepositRepository;
import com.wallet.repositories.TransactionRepository;
import com.wallet.utls.Feature;
import com.wallet.utls.RandomStringExample;



@Entity
@Table(name="tbl_deposit")
public class Deposit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="deposit_id")
	private int deposit_id;
	
	@Column(name="transaction_code")
	private String transactioncode;
	
	@Column(name="member_id")
	private  int member_id;
	
	@Column(name="deposit_amount")
	private BigDecimal deposit_amount;
	
	@Column(name="date_time")
	private Date date_time;
	
	@Column(name="status")
	private int status;
	
	@Column(name="note")
	private String note;
	

	public int getDeposit_id() {
		return deposit_id;
	}

	public void setDeposit_id(int deposit_id) {
		this.deposit_id = deposit_id;
	}

	public String getTransaction_code() {
		return transactioncode;
	}

	public void setTransaction_code(String transaction_code) {
		this.transactioncode = transaction_code;
	}

	public int getMember_id() {
		return member_id;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public BigDecimal getDeposit_amount() {
		return deposit_amount;
	}

	public void setDeposit_amount(BigDecimal deposit_amount) {
		this.deposit_amount = deposit_amount;
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

	public Deposit( String transaction_code, int member_id, BigDecimal deposit_amount, Date date_time,
			int status, String note) {
		super();
		
		this.transactioncode = transaction_code;
		this.member_id = member_id;
		this.deposit_amount = deposit_amount;
		this.date_time = date_time;
		this.status = status;
		this.note = note;
	}

	public Deposit() {
		super();
	}
	public static void doTransfer(int id_member,BigDecimal amount_money,int stt,String preHash,Card card,CardRepository cardRepository,
			DepositRepository depositRepository,TransactionRepository transactionRepository) {
		Deposit newDeposit = new Deposit(RandomStringExample.create_codeTrans(), id_member, amount_money,
				new Date(), 0, "");

		Transaction_block newTrans = new Transaction_block(stt + 1, "", preHash,
				Feature.getJsonObjectDeposit(newDeposit), id_member, 1, newDeposit.getTransaction_code());
		newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));

		card.setBalance(card.getBalance().add(amount_money));
		cardRepository.save(card);
		depositRepository.save(newDeposit);
		transactionRepository.save(newTrans);

	}
	
}
