package com.wallet.Entitis;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="tbl_currency_supported")
public class Currency_supported {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name="currency_id")
	private int currency_id;
	
	@Column(name="currency_name")
	private String currency_name;
	
	
	@Column(name="usd_equivalent")
	private BigDecimal usd_equivalent;


	public int getCurrency_id() {
		return currency_id;
	}


	public void setCurrency_id(int currency_id) {
		this.currency_id = currency_id;
	}


	public String getCurrency_name() {
		return currency_name;
	}


	public void setCurrency_name(String currency_name) {
		this.currency_name = currency_name;
	}


	public BigDecimal getUsd_equivalent() {
		return usd_equivalent;
	}


	public void setUsd_equivalent(BigDecimal usd_equivalent) {
		this.usd_equivalent = usd_equivalent;
	}


	public Currency_supported(String currency_name, BigDecimal usd_equivalent) {
		super();
		this.currency_name = currency_name;
		this.usd_equivalent = usd_equivalent;
	}


	public Currency_supported() {
		super();
	}
	
	
	

}
