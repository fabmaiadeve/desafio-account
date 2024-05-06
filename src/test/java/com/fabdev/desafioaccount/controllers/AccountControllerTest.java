package com.fabdev.desafioaccount.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fabdev.desafioaccount.dtos.AccountDto;
import com.fabdev.desafioaccount.dtos.MonetaryTransactionDto;
import com.fabdev.desafioaccount.exceptions.NegativeValueException;
import com.fabdev.desafioaccount.exceptions.NotFoundObjetException;
import com.fabdev.desafioaccount.exceptions.NotNullableFieldsException;
import com.fabdev.desafioaccount.models.Account;
import com.fabdev.desafioaccount.models.MonetaryTransaction;
import com.fabdev.desafioaccount.services.AccountService;
import com.fabdev.desafioaccount.services.MonetaryTransactionService;

class AccountControllerTest {

	@Mock private AccountService service;
	@InjectMocks AccountController controller;
	@Mock private MonetaryTransactionService monetaryTransactionService;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	public void testCadastrarContaDeveRetornar201Created() {

		AccountDto validAccountDto = new AccountDto("Ros", true, new BigDecimal(1000), "cc04005", LocalDate.of(1945, 2, 22));
		Account validAccountSave = new Account("Ros", true, new BigDecimal(1000), "cc04005", LocalDate.of(1945, 2, 22));
		
		when(service.saveAccount(validAccountDto)).thenReturn(validAccountSave);
		
		ResponseEntity<Account> response = controller.cadastrarConta(validAccountDto);
		
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(validAccountSave, response.getBody());
	}

	@Test
	public void testCadastrarContaDeveRetornar406NotAcceptable() {

		AccountDto invalidAccountDto = null;
		
		doThrow(NotNullableFieldsException.class).when(service).saveAccount(null);
		
		assertThrows(NotNullableFieldsException.class, () -> {
	        controller.cadastrarConta(invalidAccountDto);
	    });
	}
	
	@Test
	public void testGetAllAcountsDeveRetornar200Ok() {
		
		Account validAccount = new Account("Ros", true, new BigDecimal(1000), "cc04005", LocalDate.of(1945, 2, 22));
		List<Account> validListaAccount = new ArrayList<>();
		validListaAccount.add(validAccount);
		
		when(service.listAllAccounts()).thenReturn(validListaAccount);
		
		ResponseEntity<List<Account>> response = controller.getAllAccounts();
				
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(validListaAccount, response.getBody());
	}
	
	@Test
	public void testFindPageDeveRetornar200Ok() {
		
		Account validAccount = new Account("Ros", true, new BigDecimal(1000), "cc04005", LocalDate.of(1945, 2, 22));
		List<Account> validListaAccount = new ArrayList<>();
		validListaAccount.add(validAccount);
		
		int totalAccounts = validListaAccount.size();
		int pageNumber = 0;
		int linesPerPage = 3;
		
		Pageable pageable = PageRequest.of(pageNumber, linesPerPage);
		Page<Account> expectedPageAccount = new PageImpl<>(validListaAccount, pageable, totalAccounts);
		
		when(service.findByPageAccount(0, 3, "id", "ASC")).thenReturn(expectedPageAccount);
		
		ResponseEntity<Page<Account>> response = controller.findPage(0, 3, "id", "ASC");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(expectedPageAccount, response.getBody());
	}
	
	@Test
	public void testFindOneAccountDeveRetornar200Ok() {
		
		Long idValid = 1L;
		Account validAccount = new Account();
		validAccount.setId(1L);
		Optional<Account> expectedOpt = Optional.of(validAccount);
		
		when(service.getById(validAccount.getId())).thenReturn(expectedOpt);
		
		ResponseEntity<Account> response = controller.findOneAccount(idValid);
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(expectedOpt.get(), response.getBody());
	}
	
	@Test
	public void testFindOneAccountDeveRetornar404NotFound() {
		
		Long idInvalid = 99L;
		
		doThrow(NotFoundObjetException.class).when(service).getById(idInvalid);
		
		assertThrows(NotFoundObjetException.class, () -> {
			controller.findOneAccount(idInvalid);
		});
	}
	
	@Test
	public void testMakeDepositDeveRetornar204NoContent() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto(new BigDecimal(100));
		
		Account validAccount = new Account();
		validAccount.setBalance(new BigDecimal(1000));
		validAccount.setId(1L);
		
		Account expectedAccount = new Account();
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		when(service.doDeposit(validAccount, monetaryTransactionDto)).thenReturn(expectedAccount);
		
		ResponseEntity<Account> response = controller.makeDeposit(1L, monetaryTransactionDto);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(expectedAccount, response.getBody());
	}
	
	@Test
	public void testMakeWithdrawalDeveRetornar204NoContent() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto(new BigDecimal(100));
		
		Account validAccount = new Account();
		validAccount.setExclusivePlan(true);
		validAccount.setBalance(new BigDecimal(1000));
		validAccount.setId(1L);
		
		Account expectedAccount = new Account("Ros", true, new BigDecimal(900), "cc04005", LocalDate.of(1945, 2, 22));
		expectedAccount.setId(1L);
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		when(service.doDeposit(validAccount, monetaryTransactionDto)).thenReturn(expectedAccount);
		
		ResponseEntity<Account> response = controller.makeWithdrawal(1L, monetaryTransactionDto);
		
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	    assertNull(response.getBody());
	}
	
	@Test
	public void testMakeDepositDeveRetornar406NotAcceptable() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto();
		monetaryTransactionDto.setTransactionAmount(null);
		Account validAccount = new Account();
		validAccount.setId(1L);
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		doThrow(NotNullableFieldsException.class).when(service).doDeposit(validAccount, monetaryTransactionDto);
		
		assertThrows(NotNullableFieldsException.class, () -> {
	        controller.makeDeposit(1L, monetaryTransactionDto);
	    });
	}
	
	@Test
	public void testMakeWithdrawalDeveRetornar406NotAcceptable() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto();
		monetaryTransactionDto.setTransactionAmount(null);
		Account validAccount = new Account();
		validAccount.setId(1L);
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		doThrow(NotNullableFieldsException.class).when(service).doWithdrawal(validAccount, monetaryTransactionDto);
		
		assertThrows(NotNullableFieldsException.class, () -> {
			controller.makeWithdrawal(1L, monetaryTransactionDto);
	    });
	}
	
	@Test
	public void testMakeDepositIgualZeroDeveRetornar409Conflict() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto();
		monetaryTransactionDto.setTransactionAmount(new BigDecimal(0));
		Account validAccount = new Account();
		validAccount.setId(1L);
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		doThrow(NegativeValueException.class).when(service).doDeposit(validAccount, monetaryTransactionDto);
		
		assertThrows(NegativeValueException.class, () -> {
	        controller.makeDeposit(1L, monetaryTransactionDto);
	    });
	}
	
	@Test
	public void testMakeWithdrawalIgualZeroDeveRetornar409Conflict() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto();
		monetaryTransactionDto.setTransactionAmount(new BigDecimal(0));
		Account validAccount = new Account();
		validAccount.setId(1L);
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		doThrow(NegativeValueException.class).when(service).doWithdrawal(validAccount, monetaryTransactionDto);
		
		assertThrows(NegativeValueException.class, () -> {
			controller.makeWithdrawal(1L, monetaryTransactionDto);
	    });
	}
	
	@Test
	public void testMakeDepositMenorQueZeroDeveRetornar409Conflict() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto();
		monetaryTransactionDto.setTransactionAmount(new BigDecimal(-100));
		Account validAccount = new Account();
		validAccount.setId(1L);
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		doThrow(NegativeValueException.class).when(service).doDeposit(validAccount, monetaryTransactionDto);
		
		assertThrows(NegativeValueException.class, () -> {
	        controller.makeDeposit(1L, monetaryTransactionDto);
	    });
	}
	
	@Test
	public void testMakeWithdrawalMenorQueZeroDeveRetornar409Conflict() {
		
		MonetaryTransactionDto monetaryTransactionDto = new MonetaryTransactionDto();
		monetaryTransactionDto.setTransactionAmount(new BigDecimal(-100));
		Account validAccount = new Account();
		validAccount.setId(1L);
		
		when(service.getById(validAccount.getId())).thenReturn(Optional.of(validAccount));
		doThrow(NegativeValueException.class).when(service).doWithdrawal(validAccount, monetaryTransactionDto);
		
		assertThrows(NegativeValueException.class, () -> {
			controller.makeWithdrawal(1L, monetaryTransactionDto);
	    });
	}
	
	@Test
	public void testHystoricTransactionDeveRetornar200Ok() {
		
		MonetaryTransaction monetaryTransaction = new MonetaryTransaction("d", new BigDecimal(100), LocalDateTime.of(2024, 05, 02, 10, 10, 10), 1L);
		List<MonetaryTransaction> validListaMonetaryTransaction = new ArrayList<>();
		validListaMonetaryTransaction.add(monetaryTransaction);
		
		int totalMonetaryTransactions = validListaMonetaryTransaction.size();
		int pageNumber = 0;
		int linesPerPage = 3;
		
		Pageable pageable = PageRequest.of(pageNumber, linesPerPage);
		Page<MonetaryTransaction> expectedPageMonetaryTransaction = new PageImpl<>(validListaMonetaryTransaction, pageable, totalMonetaryTransactions);
		
		when(monetaryTransactionService.findByPageMonetaryTransaction(1L, 0, 3, "id", "ASC")).thenReturn(expectedPageMonetaryTransaction);
		
		ResponseEntity<Page<MonetaryTransaction>> response = controller.hystoricTransaction(1L, 0, 3, "id", "ASC");
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(expectedPageMonetaryTransaction, response.getBody());
	}
}
