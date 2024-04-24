package com.wallet.entitis;

import java.math.BigDecimal;
import java.util.Date;

public class History_tranfer {

	private String type;
	private Date time;
	private String content ;
	private String receiver;
	private BigDecimal amount;
	
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public History_tranfer( String type, Date time, String content, String receiver,BigDecimal amount) {
		super();
		
		this.type = type;
		this.time = time;
		this.content = content;
		this.receiver = receiver;
		this.amount=amount;
	}
	public History_tranfer() {
		super();
	}
	
}
