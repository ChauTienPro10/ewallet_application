package com.wallet.entitis;

import java.math.BigDecimal;

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
@Table(name="tbl_card")
public class Card {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="card_id")
	private int card_id;
	
	@Column(name="card_number")
	private String cardnumber;
	
	@Column(name="member_id")
	private int memberid;
	
	
	@Column(name="pin")
	private String pin;
	
	@Column(name="balance")
	private BigDecimal balance;

	public String getCard_number() {
		return cardnumber;
	}

	public void setCard_number(String card_number) {
		this.cardnumber = card_number;
	}

	public int getMember_id() {
		return memberid;
	}

	public void setMember_id(int member_id) {
		this.memberid = member_id;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Card(String card_number, int member_id, String pin, BigDecimal balance) {
		super();
		this.cardnumber = card_number;
		this.memberid = member_id;
		this.pin = pin;
		this.balance = balance;
	}

	public Card() {
		
	}
	

	
}
