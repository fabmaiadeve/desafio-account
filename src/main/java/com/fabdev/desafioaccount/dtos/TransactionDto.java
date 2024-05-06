package com.fabdev.desafioaccount.dtos;

import java.math.BigDecimal;

public class TransactionDto {
	
	private String typeTransaction;
	
	private BigDecimal valueTransaction;
	
	private Long userId;

	public TransactionDto() {
	}
	
	public TransactionDto(String typeTransaction, BigDecimal valueTransaction, Long userId) {
		this.typeTransaction = typeTransaction;
		this.valueTransaction = valueTransaction;
		this.userId = userId;
	}

	public String getTypeTransaction() {
		return typeTransaction;
	}

	public void setTypeTransaction(String typeTransaction) {
		this.typeTransaction = typeTransaction;
	}

	public BigDecimal getValueTransaction() {
		return valueTransaction;
	}

	public void setValueTransaction(BigDecimal valueTransaction) {
		this.valueTransaction = valueTransaction;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
