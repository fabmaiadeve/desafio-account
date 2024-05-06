package com.fabdev.desafioaccount.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fabdev.desafioaccount.dtos.TransactionDto;
import com.fabdev.desafioaccount.exceptions.NotFoundObjetException;
import com.fabdev.desafioaccount.models.MonetaryTransaction;
import com.fabdev.desafioaccount.repositories.MonetaryTransactionRepository;

@Service
public class MonetaryTransactionService {

	@Autowired
	private MonetaryTransactionRepository monetaryTransactionRepository;
	
	public MonetaryTransactionService(MonetaryTransactionRepository monetaryTransactionRepository) {
		this.monetaryTransactionRepository = monetaryTransactionRepository;
	}
	
	@Transactional
	public void saveMonetaryTransaction(TransactionDto transactionDto) {
		MonetaryTransaction transaction = new MonetaryTransaction(transactionDto.getTypeTransaction(), transactionDto.getValueTransaction(), LocalDateTime.now(), transactionDto.getUserId());
		
		monetaryTransactionRepository.save(transaction);
	} 
	
	public Page<MonetaryTransaction> findByPageMonetaryTransaction(Long idUser, Integer page, Integer linesPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPage, Direction.valueOf(direction), orderBy);
		
		List<MonetaryTransaction> listAllTransactions = monetaryTransactionRepository.findByUserId(idUser);
		
		if(listAllTransactions.isEmpty()) {
			throw new NotFoundObjetException("O idUser: " + idUser.toString() + " não se encontra na base de dados");
		}
		
		return monetaryTransactionRepository.findAllTransactions(idUser, pageRequest);
	}

}
