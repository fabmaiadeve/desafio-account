package com.fabdev.desafioaccount.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.fabdev.desafioaccount.dtos.TransactionDto;
import com.fabdev.desafioaccount.exceptions.NotFoundObjetException;
import com.fabdev.desafioaccount.models.MonetaryTransaction;
import com.fabdev.desafioaccount.repositories.MonetaryTransactionRepository;

@ExtendWith(MockitoExtension.class)
class MonetaryTransactionServiceTest {

	@Mock private MonetaryTransactionRepository rep;
	@InjectMocks private MonetaryTransactionService service;
	
	private TransactionDto validTransactionDto;
	private MonetaryTransaction validMonetaryTransaction;
	private List<MonetaryTransaction> expectedListaMonetaryTransaction = new ArrayList<>();
	private List<MonetaryTransaction> expectedListaVaziaMonetaryTransaction = new ArrayList<>();
	private PageRequest validPageRequest; 
	private Page<MonetaryTransaction> expectedPageMonetaryTransaction;
	
	@BeforeEach
	public void setUp() {
		validTransactionDto = new TransactionDto("d", BigDecimal.valueOf(100), 1L);
		validMonetaryTransaction = new MonetaryTransaction("d", BigDecimal.valueOf(100), LocalDateTime.of(2024, 05, 02, 10, 10, 10), 1L);
		validPageRequest = PageRequest.of(0, 3, Direction.valueOf("ASC"), "id");
	}

	@Test
	public void testSalvarMonetaryTransactionComSucesso() {
		
		service.saveMonetaryTransaction(validTransactionDto);
		
		verify(rep).save(
			argThat(transaction ->
				transaction.getTypeTransaction().equals(validTransactionDto.getTypeTransaction()) &&
				transaction.getValueTransaction().equals(validTransactionDto.getValueTransaction()) &&
				transaction.getUserId().equals(validTransactionDto.getUserId())
			)
		);		
	}
	
	@Test
	public void testDeveRetornarListaMonetaryTransactionPaginadaComSucesso() {

		expectedListaMonetaryTransaction.add(validMonetaryTransaction);
		
		int totalTransactions = expectedListaMonetaryTransaction.size(); 
		int pageNumber = 0;
		int linesPerPage = 3;
		
		Pageable pageable = PageRequest.of(pageNumber, linesPerPage);
		expectedPageMonetaryTransaction = new PageImpl<>(expectedListaMonetaryTransaction, pageable, totalTransactions);
		
		when(rep.findByUserId(validTransactionDto.getUserId())).thenReturn(expectedListaMonetaryTransaction);
		when(rep.findAllTransactions(validTransactionDto.getUserId(), validPageRequest)).thenReturn(expectedPageMonetaryTransaction);
		
		Page<MonetaryTransaction> savedPageMonetaryTransaction = service.findByPageMonetaryTransaction(1L, 0, 3, "id", "ASC");
		
		assertNotNull(savedPageMonetaryTransaction);
		assertEquals(expectedPageMonetaryTransaction, savedPageMonetaryTransaction);
	}
	
	@Test
	public void testDeveRetornarExceptionNotFoundObjetExceptionQdoNaoExisteIdUser() {
		
		Long invalidIdUser = 99L;
			
		when(rep.findByUserId(invalidIdUser)).thenReturn(expectedListaVaziaMonetaryTransaction);
		
		NotFoundObjetException exception = assertThrows(NotFoundObjetException.class, () -> service.findByPageMonetaryTransaction(99L, 0, 3, "id", "ASC"));
	    assertEquals("O idUser: 99 não se encontra na base de dados", exception.getMessage());
	}
}
