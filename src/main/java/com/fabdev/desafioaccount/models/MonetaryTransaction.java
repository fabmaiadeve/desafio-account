package com.fabdev.desafioaccount.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="tb_monetary_transaction")
public class MonetaryTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String typeTransaction;
	
	private BigDecimal valueTransaction;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS][.SS][.S]")
	private LocalDateTime dateOfTransactioan;
	
	private Long userId;
	
	public MonetaryTransaction() {
	}

	public MonetaryTransaction(String typeTransaction, BigDecimal valueTransaction,
			LocalDateTime dateOfTransactioan, Long userId) {
		this.typeTransaction = typeTransaction;
		this.valueTransaction = valueTransaction;
		this.dateOfTransactioan = dateOfTransactioan;
		this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getDateOfTransactioan() {
		return dateOfTransactioan;
	}

	public void setDateOfTransactioan(LocalDateTime dateOfTransactioan) {
		this.dateOfTransactioan = dateOfTransactioan;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "MonetaryTransaction [id=" + id + ", typeTransaction=" + typeTransaction + ", valueTransaction="
				+ valueTransaction + ", dateOfTransactioan=" + dateOfTransactioan + ", userId=" + userId + "]";
	}
}
