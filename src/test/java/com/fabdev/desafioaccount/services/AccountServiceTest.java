package com.fabdev.desafioaccount.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import com.fabdev.desafioaccount.dtos.AccountDto;
import com.fabdev.desafioaccount.dtos.MonetaryTransactionDto;
import com.fabdev.desafioaccount.dtos.TransactionDto;
import com.fabdev.desafioaccount.exceptions.NegativeValueException;
import com.fabdev.desafioaccount.exceptions.NotFoundObjetException;
import com.fabdev.desafioaccount.exceptions.NotNullableFieldsException;
import com.fabdev.desafioaccount.models.Account;
import com.fabdev.desafioaccount.repositories.AccountRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
	
	@Mock private AccountRepository rep;
	@InjectMocks private AccountService service;
	@Mock private MonetaryTransactionService monetaryTransactionService;
	
	private AccountDto validAccountDto;
	private Account validAccount;
	private List<Account> expectedListaAccount = new ArrayList<>();
	private PageRequest validPageRequest; 
	private Page<Account> expectedPageAccount;
	private MonetaryTransactionDto validMonetaryTransactionDto;
	private MonetaryTransactionDto validMonetaryTransactionDtoAbaixoDeCem;
	private MonetaryTransactionDto validMonetaryTransactionDtoTrezento;
	private MonetaryTransactionDto validMonetaryTransactionDtoAbaixoDeTrezento;
	private MonetaryTransactionDto validMonetaryTransactionDtoAcimaDeTrezento;
	
	@BeforeEach
	public void setUp() {
		validAccountDto = new AccountDto("Ros", true, new BigDecimal(1000), "cc04005", LocalDate.of(1945, 2, 22));
		validAccount = new Account("Ros", true, new BigDecimal(1000), "cc04005", LocalDate.of(1945, 2, 22));
		validPageRequest = PageRequest.of(0, 3, Direction.valueOf("ASC"), "id");
		validMonetaryTransactionDto = new MonetaryTransactionDto(new BigDecimal(100));
		validMonetaryTransactionDtoAbaixoDeCem = new MonetaryTransactionDto(new BigDecimal(90));
		validMonetaryTransactionDtoTrezento = new MonetaryTransactionDto(new BigDecimal(300));
		validMonetaryTransactionDtoAbaixoDeTrezento = new MonetaryTransactionDto(new BigDecimal(290));
		validMonetaryTransactionDtoAcimaDeTrezento = new MonetaryTransactionDto(new BigDecimal(310));
	}
	

	@Test
	public void testDeveSalvarContaComSucesso() {
		
		Account expectedAccount = new Account(
				validAccountDto.getName(), 
				validAccountDto.getExclusivePlan(), 
				validAccountDto.getBalance(),
				validAccountDto.getAccountNumber(),
				validAccountDto.getBirthDate());
		
		when(rep.save(any())).thenReturn(expectedAccount);
		
		Account savedAccount = service.saveAccount(validAccountDto);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
	
	@Test
	public void testDeveLancarExceptionQuandoNameEstaNulo() {
				
	    validAccountDto.setName(null);

	    NotNullableFieldsException exception = assertThrows(NotNullableFieldsException.class, () -> service.saveAccount(validAccountDto));
	    assertEquals("O campo name não pode ser nulo ou estar vazio!", exception.getMessage());
	}
	
	@Test
	public void testDeveLancarExceptionQuandoExclusivePlanEstaNulo() {
				
	    validAccountDto.setExclusivePlan(null);

	    NotNullableFieldsException exception = assertThrows(NotNullableFieldsException.class, () -> service.saveAccount(validAccountDto));
	    assertEquals("O campo exclusivePlan não pode ser nulo!", exception.getMessage());
	}
	
	@Test
	public void testDeveLancarExceptionQuandoBalanceEstaNulo() {
				
	    validAccountDto.setBalance(null);

	    NotNullableFieldsException exception = assertThrows(NotNullableFieldsException.class, () -> service.saveAccount(validAccountDto));
	    assertEquals("O campo balance não pode ser nulo!", exception.getMessage());
	}
	
	@Test
	public void testDeveLancarExceptionQuandoAccountNumberEstaNulo() {
				
	    validAccountDto.setAccountNumber(null);

	    NotNullableFieldsException exception = assertThrows(NotNullableFieldsException.class, () -> service.saveAccount(validAccountDto));
	    assertEquals("O campo accountNumber não pode ser nulo ou estar vazio!", exception.getMessage());
	}
	
	@Test
	public void testDeveLancarExceptionQuandoBirthDateEstaNulo() {
				
	    validAccountDto.setBirthDate(null);

	    NotNullableFieldsException exception = assertThrows(NotNullableFieldsException.class, () -> service.saveAccount(validAccountDto));
	    assertEquals("O campo birthDate não pode ser nulo!", exception.getMessage());
	}
	
	@Test
	public void testDeveRetornarListaAccountComSucesso() {
		
		expectedListaAccount.add(validAccount);
		
		when(rep.findAll()).thenReturn(expectedListaAccount);
		
		List<Account> savedListaAccount = service.listAllAccounts();
		
		assertNotNull(savedListaAccount);
        assertEquals(expectedListaAccount, savedListaAccount);
	}
	
	@Test
	public void testDeveRetornarListaAccountPaginadaComSucesso() {
		
		expectedListaAccount.add(validAccount);
		
		int totalAccounts = expectedListaAccount.size();
		int pageNumber = 0;
		int linesPerPage = 3;
		
		Pageable pageable = PageRequest.of(pageNumber, linesPerPage);
		expectedPageAccount = new PageImpl<>(expectedListaAccount, pageable, totalAccounts);
		
		when(rep.findAll(validPageRequest)).thenReturn(expectedPageAccount);
		
		Page<Account> savedPageAccount = service.findByPageAccount(0, 3, "id", "ASC");
		
		assertNotNull(savedPageAccount);
        assertEquals(expectedPageAccount, savedPageAccount);
	}
	
	@Test
	public void testDeveRetornarAccountPorIdComSucesso() {
		
		validAccount.setId(1L);
		Optional<Account> expectedOpt = Optional.of(validAccount);
		
		when(rep.findById(validAccount.getId())).thenReturn(expectedOpt);
		
		Optional<Account> savedOpt = service.getById(validAccount.getId());
		
		assertEquals(expectedOpt, savedOpt);
	}
	
	@Test
	public void testDeveRetornarExceptionQuandoAccountNaoEncontrado() {
		
		Long invalidId = 99L;
		
		when(rep.findById(invalidId)).thenReturn(Optional.empty());
		
		NotFoundObjetException exception = assertThrows(NotFoundObjetException.class, () -> service.getById(invalidId));
	    assertEquals("O id: 99 não se encontra na base de dados", exception.getMessage());
	}
	
	@Test
	public void testDeveRealizarDepositoComSucesso() {
		
		Account expectedAccount = new Account();
		expectedAccount.setId(1L);
		BigDecimal validNewBalance = validAccount.getBalance().add(validMonetaryTransactionDto.getTransactionAmount());
		expectedAccount.setBalance(validNewBalance);
				
		when(rep.save(any())).thenReturn(expectedAccount);
		doNothing().when(monetaryTransactionService).saveMonetaryTransaction(any(TransactionDto.class));
		
		Account savedAccount = service.doDeposit(validAccount, validMonetaryTransactionDto);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
	
	@Test
	public void testDeveLancarExceptionQuandoTransactionAmountEstaNulo() {
		
		validMonetaryTransactionDto.setTransactionAmount(null);
		
		NotNullableFieldsException exception = assertThrows(NotNullableFieldsException.class, () -> service.doDeposit(validAccount, validMonetaryTransactionDto));
	    assertEquals("O campo valor de depósito/saque não pode ser nulo!", exception.getMessage());
	}
	
	@Test
	public void testDeveLancarExceptionQuandoTransactionAmountEhNegativo() {
		
		validMonetaryTransactionDto.setTransactionAmount(new BigDecimal(-100));
		
		NegativeValueException exception = assertThrows(NegativeValueException.class, () -> service.doDeposit(validAccount, validMonetaryTransactionDto));
	    assertEquals("O campo valor de depósito/saque não pode ser negativo ou igual a zero!", exception.getMessage());
	}
	
	@Test
	public void testDeveLancarExceptionQuandoTransactionAmountEhZero() {
		
		validMonetaryTransactionDto.setTransactionAmount(new BigDecimal(0));
		
		NegativeValueException exception = assertThrows(NegativeValueException.class, () -> service.doDeposit(validAccount, validMonetaryTransactionDto));
	    assertEquals("O campo valor de depósito/saque não pode ser negativo ou igual a zero!", exception.getMessage());
	}
	
	@Test
	public void testDeveRealizarSaqueQuandoExclusivePlanEhTrue() {
		
		Account expectedAccount = new Account();
		expectedAccount.setId(1L);
		BigDecimal validNewBalance = validAccount.getBalance().subtract(validMonetaryTransactionDto.getTransactionAmount());
		expectedAccount.setBalance(validNewBalance);
		
		when(rep.save(any())).thenReturn(expectedAccount);
		doNothing().when(monetaryTransactionService).saveMonetaryTransaction(any(TransactionDto.class));
		
		Account savedAccount = service.doWithdrawal(validAccount, validMonetaryTransactionDto);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
	
	@Test
	public void testDeveRealizarSaqueQuandoExclusivePlanEhFalseAndValorAbaixoDeCem() {
		
		Account expectedAccount = new Account();
		expectedAccount.setId(1L);
		Boolean exPlanFalse = false;
		expectedAccount.setExclusivePlan(exPlanFalse);
		BigDecimal validNewBalance = validAccount.getBalance().subtract(validMonetaryTransactionDto.getTransactionAmount());
		expectedAccount.setBalance(validNewBalance);
		
		when(rep.save(any())).thenReturn(expectedAccount);
		doNothing().when(monetaryTransactionService).saveMonetaryTransaction(any(TransactionDto.class));
		
		Account savedAccount = service.doWithdrawal(validAccount, validMonetaryTransactionDtoAbaixoDeCem);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
	
	@Test
	public void testDeveRealizarSaqueQuandoExclusivePlanEhFalseAndValorIgualCem() {
		
		Account expectedAccount = new Account();
		expectedAccount.setId(1L);
		Boolean exPlanFalse = false;
		expectedAccount.setExclusivePlan(exPlanFalse);
		BigDecimal validNewBalance = validAccount.getBalance().subtract(validMonetaryTransactionDto.getTransactionAmount());
		expectedAccount.setBalance(validNewBalance);
		
		when(rep.save(any())).thenReturn(expectedAccount);
		doNothing().when(monetaryTransactionService).saveMonetaryTransaction(any(TransactionDto.class));
		
		Account savedAccount = service.doWithdrawal(validAccount, validMonetaryTransactionDto);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
	
	@Test
	public void testDeveRealizarSaqueQuandoExclusivePlanEhFalseAndValorAbaixoDeTrezento() {
		
		Account expectedAccount = new Account();
		expectedAccount.setId(1L);
		Boolean exPlanFalse = false;
		expectedAccount.setExclusivePlan(exPlanFalse);
		BigDecimal validNewBalance = validAccount.getBalance().subtract(validMonetaryTransactionDto.getTransactionAmount());
		expectedAccount.setBalance(validNewBalance);
		
		when(rep.save(any())).thenReturn(expectedAccount);
		doNothing().when(monetaryTransactionService).saveMonetaryTransaction(any(TransactionDto.class));
		
		Account savedAccount = service.doWithdrawal(validAccount, validMonetaryTransactionDtoAbaixoDeTrezento);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
	
	@Test
	public void testDeveRealizarSaqueQuandoExclusivePlanEhFalseAndValorIgualTrezento() {
		
		Account expectedAccount = new Account();
		expectedAccount.setId(1L);
		Boolean exPlanFalse = false;
		expectedAccount.setExclusivePlan(exPlanFalse);
		BigDecimal validNewBalance = validAccount.getBalance().subtract(validMonetaryTransactionDto.getTransactionAmount());
		expectedAccount.setBalance(validNewBalance);
		
		when(rep.save(any())).thenReturn(expectedAccount);
		doNothing().when(monetaryTransactionService).saveMonetaryTransaction(any(TransactionDto.class));
		
		Account savedAccount = service.doWithdrawal(validAccount, validMonetaryTransactionDtoTrezento);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
	
	@Test
	public void testDeveRealizarSaqueQuandoExclusivePlanEhFalseAndValorAcimaDeTrezento() {
		
		Account expectedAccount = new Account();
		expectedAccount.setId(1L);
		Boolean exPlanFalse = false;
		expectedAccount.setExclusivePlan(exPlanFalse);
		BigDecimal validNewBalance = validAccount.getBalance().subtract(validMonetaryTransactionDto.getTransactionAmount());
		expectedAccount.setBalance(validNewBalance);
		
		when(rep.save(any())).thenReturn(expectedAccount);
		doNothing().when(monetaryTransactionService).saveMonetaryTransaction(any(TransactionDto.class));
		
		Account savedAccount = service.doWithdrawal(validAccount, validMonetaryTransactionDtoAcimaDeTrezento);
		
		assertNotNull(savedAccount);
        assertEquals(expectedAccount, savedAccount);
	}
}
