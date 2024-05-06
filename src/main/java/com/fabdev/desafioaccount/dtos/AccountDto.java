package com.fabdev.desafioaccount.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDto {
	
	private Long id;
	private String name;
	private Boolean exclusivePlan;
	private BigDecimal balance;
	private String accountNumber;
	private LocalDate birthDate;

	public AccountDto(){
	}

	public AccountDto(String name, Boolean exclusivePlan, BigDecimal balance, String accountNumber,
			LocalDate birthDate) {
		this.name = name;
		this.exclusivePlan = exclusivePlan;
		this.balance = balance;
		this.accountNumber = accountNumber;
		this.birthDate = birthDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getExclusivePlan() {
		return exclusivePlan;
	}

	public void setExclusivePlan(Boolean exclusivePlan) {
		this.exclusivePlan = exclusivePlan;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public String toString() {
		return "AccountDto [id=" + id + ", name=" + name + ", exclusivePlan=" + exclusivePlan + ", balance=" + balance
				+ ", accountNumber=" + accountNumber + ", birthDate=" + birthDate + "]";
	}
}
