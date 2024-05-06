package com.fabdev.desafioaccount.dtos;

import java.math.BigDecimal;

public class MonetaryTransactionDto {
	
	private BigDecimal transactionAmount;
	
	public MonetaryTransactionDto() {
	}

	public MonetaryTransactionDto(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
}
