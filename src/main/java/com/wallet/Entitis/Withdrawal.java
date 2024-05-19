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
import com.wallet.repositories.TransactionRepository;
import com.wallet.repositories.WithdrawalRepository;
import com.wallet.utls.Feature;
import com.wallet.utls.RandomStringExample;



@Entity
@Table(name="tbl_withdrawal")
public class Withdrawal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="withdrawal_id")
	private int withdrawal_id;
	
	@Column(name="transaction_code")
	private String transactioncode;
	
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
		this.transactioncode = transaction_code;
		this.member_id = member_id;
		this.amount = amount;
		this.date_time = date_time;
		this.status = status;
		this.comments = comments;
	}

	public Withdrawal() {
		super();
	}
	
	public static boolean dowithdraw(Member member,CardRepository cardRepository,BigDecimal amount_money,TransactionRepository transactionRepository
			, WithdrawalRepository withdrawalRepository) {
		Card card = cardRepository.findByMemberid(member.getMember_id());
		if (card == null) {
			return false;
		}
		if (amount_money.compareTo(card.getBalance()) > 0) {
			return false;
		}
		int stt = 0;
		String preHash = null;
		if (transactionRepository.count() != 0) {
			stt = (int) transactionRepository.count();
			Transaction_block transaction = transactionRepository.findByBlockid(stt);
			preHash = transaction.getHash_block();
		}
		Withdrawal withdrawal = new Withdrawal(RandomStringExample.create_codeTrans(), member.getMember_id(), amount_money, new Date(),
				2, "");
		Transaction_block newTrans = new Transaction_block(stt + 1, "", preHash,
				Feature.getJsonObjectWithdraw(withdrawal), member.getMember_id(), 2, withdrawal.getTransaction_code());
		newTrans.setHash_block(Feature.calculateSHA256Hash(Feature.getJsonObjectTranSaction(newTrans)));
		card.setBalance(card.getBalance().subtract(amount_money));
		withdrawalRepository.save(withdrawal);
		transactionRepository.save(newTrans);
		cardRepository.save(card);
		return true;

	}
	
}
