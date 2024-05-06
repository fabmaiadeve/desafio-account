package com.fabdev.desafioaccount.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fabdev.desafioaccount.dtos.AccountDto;
import com.fabdev.desafioaccount.dtos.MonetaryTransactionDto;
import com.fabdev.desafioaccount.dtos.TransactionDto;
import com.fabdev.desafioaccount.exceptions.NegativeValueException;
import com.fabdev.desafioaccount.exceptions.NotFoundObjetException;
import com.fabdev.desafioaccount.exceptions.NotNullableFieldsException;
import com.fabdev.desafioaccount.models.Account;
import com.fabdev.desafioaccount.repositories.AccountRepository;

@Service
public class AccountService {
	
	public static final String DEPOSIT = "d";
	public static final String WITHDRAWAL = "w";
	public static final int ABAIXO_DE_100 = 100;
	public static final double TAXA01 = 0.004;
	public static final int ABAIXO_DE_300 = 300;
	public static final double TAXA02 = 0.01;
		
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private MonetaryTransactionService monetaryTransactionService;
	
	public AccountService(AccountRepository accountRepository, MonetaryTransactionService monetaryTransactionService) {
		this.accountRepository = accountRepository;
		this.monetaryTransactionService = monetaryTransactionService;
	}
	
	@Transactional
	public Account saveAccount(AccountDto accountDto) {
		
		Account account = new Account(
				accountDto.getName(), 
				accountDto.getExclusivePlan(), 
				accountDto.getBalance(),
				accountDto.getAccountNumber(),
				accountDto.getBirthDate());
		
		validateFields(account);
		
		return accountRepository.save(account);
	}

	private void validateFields(Account account) {
		
		if(account.getName() == null || account.getName().isBlank()) {
			throw new NotNullableFieldsException("O campo name não pode ser nulo ou estar vazio!");
		} else if(account.getExclusivePlan() == null) {
			throw new NotNullableFieldsException("O campo exclusivePlan não pode ser nulo!");
		} else if(account.getBalance() == null) {
			throw new NotNullableFieldsException("O campo balance não pode ser nulo!");
		} else if(account.getAccountNumber() == null || account.getAccountNumber().isBlank()) {
			throw new NotNullableFieldsException("O campo accountNumber não pode ser nulo ou estar vazio!");
		} else if(account.getBirthDate() == null) {
			throw new NotNullableFieldsException("O campo birthDate não pode ser nulo!");
		}
	}
	
	public List<Account> listAllAccounts() {
		return accountRepository.findAll();
	}
	
	public Page<Account> findByPageAccount(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return accountRepository.findAll(pageRequest);
	}
	
	public Optional<Account> getById(Long id) {
		
		Optional<Account> accountOpt = accountRepository.findById(id); 
		
		if(accountOpt.isEmpty()) {
			throw new NotFoundObjetException("O id: " + id.toString() + " não se encontra na base de dados");
		}
		
		return accountOpt;
	}

	public Account doDeposit(Account account, MonetaryTransactionDto monetaryTransactionDto) {
		
		validatedMonetaryTransactionField(monetaryTransactionDto);
		
		BigDecimal newBalance = account.getBalance().add(monetaryTransactionDto.getTransactionAmount());
		
		Account updAccount = new Account(
				account.getName(), 
				account.getExclusivePlan(), 
				newBalance,
				account.getAccountNumber(),
				account.getBirthDate());
		updAccount.setId(account.getId());
		
		makeHistoricTransaction(DEPOSIT, monetaryTransactionDto.getTransactionAmount(), account.getId());
		
		return accountRepository.save(updAccount);
	}

	public Account doWithdrawal(Account account, MonetaryTransactionDto monetaryTransactionDto) {
		
		validatedMonetaryTransactionField(monetaryTransactionDto);
		
		BigDecimal newBalance = new BigDecimal(0);
		
		if (account.getExclusivePlan()) {
			newBalance = account.getBalance().subtract(monetaryTransactionDto.getTransactionAmount());
		} else {
			BigDecimal faixa_100 = new BigDecimal(ABAIXO_DE_100);
			BigDecimal faixa_300 = new BigDecimal(ABAIXO_DE_300);
			
			if(monetaryTransactionDto.getTransactionAmount().compareTo(faixa_100) == 0 ||
			   monetaryTransactionDto.getTransactionAmount().compareTo(faixa_100) == -1) {
				
				newBalance = account.getBalance().subtract(monetaryTransactionDto.getTransactionAmount());
				
			} else if(monetaryTransactionDto.getTransactionAmount().compareTo(faixa_300) == -1 ||
					  monetaryTransactionDto.getTransactionAmount().compareTo(faixa_300) == 0) {
				
				BigDecimal taxa01 = new BigDecimal(TAXA01);
				BigDecimal collectionFee = monetaryTransactionDto.getTransactionAmount().multiply(taxa01);
				BigDecimal billingAmount = monetaryTransactionDto.getTransactionAmount().add(collectionFee);
				
				newBalance = account.getBalance().subtract(billingAmount);
				
			} else {
				
				BigDecimal taxa02 = new BigDecimal(TAXA02);
				BigDecimal collectionFee = monetaryTransactionDto.getTransactionAmount().multiply(taxa02);
				BigDecimal billingAmount = monetaryTransactionDto.getTransactionAmount().add(collectionFee);
				
				newBalance = account.getBalance().subtract(billingAmount);				
			}
		}
		
		Account updAccount = new Account(
				account.getName(), 
				account.getExclusivePlan(), 
				newBalance,
				account.getAccountNumber(),
				account.getBirthDate());
		updAccount.setId(account.getId());
		
		makeHistoricTransaction(WITHDRAWAL, monetaryTransactionDto.getTransactionAmount(), account.getId());
			
		return accountRepository.save(updAccount);
	}
	
	private void validatedMonetaryTransactionField(MonetaryTransactionDto monetaryTransactionDto) {
		if(monetaryTransactionDto.getTransactionAmount() == null) {
			throw new NotNullableFieldsException("O campo valor de depósito/saque não pode ser nulo!");
		}
		
		if(monetaryTransactionDto.getTransactionAmount().compareTo(new BigDecimal("0")) < 0 ||
				monetaryTransactionDto.getTransactionAmount().compareTo(new BigDecimal("0")) == 0) {
			throw new NegativeValueException("O campo valor de depósito/saque não pode ser negativo ou igual a zero!");
		}
	}
	
	private void makeHistoricTransaction(String typeTransaction, BigDecimal amount, Long userId) {
		TransactionDto transactionDto = new TransactionDto(typeTransaction, amount, userId);
		
		monetaryTransactionService.saveMonetaryTransaction(transactionDto);
	}
}
