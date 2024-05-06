package com.fabdev.desafioaccount.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fabdev.desafioaccount.models.MonetaryTransaction;

@Repository
public interface MonetaryTransactionRepository extends JpaRepository<MonetaryTransaction, Long> {
	
	@Query(value = "SELECT * FROM TB_MONETARY_TRANSACTION m WHERE m.USER_ID = ?1", nativeQuery = true)
	Page<MonetaryTransaction> findAllTransactions(Long idUser, PageRequest pageRequest);
	
	List<MonetaryTransaction> findByUserId(Long userId);
}
