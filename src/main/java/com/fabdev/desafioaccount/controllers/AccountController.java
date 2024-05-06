package com.fabdev.desafioaccount.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fabdev.desafioaccount.dtos.AccountDto;
import com.fabdev.desafioaccount.dtos.MonetaryTransactionDto;
import com.fabdev.desafioaccount.models.Account;
import com.fabdev.desafioaccount.models.MonetaryTransaction;
import com.fabdev.desafioaccount.services.AccountService;
import com.fabdev.desafioaccount.services.MonetaryTransactionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Account Controller", description = "Endpoints relacionados a Conta")
@RequestMapping("/conta")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MonetaryTransactionService monetaryTransactionService;
	
	public AccountController(AccountService accountService, MonetaryTransactionService monetaryTransactionService) {
		this.accountService = accountService;
		this.monetaryTransactionService = monetaryTransactionService;
	}

	@ApiOperation(value = "Endpoint que salva dados de uma conta.")
	@PostMapping
	public ResponseEntity<Account> cadastrarConta(@RequestBody AccountDto accountDto) {
		
		return ResponseEntity.status(HttpStatus.CREATED).body(accountService.saveAccount(accountDto));
	}
	
	@ApiOperation(value = "Retorna uma lista todas as contas cadastradas.")
	@GetMapping
	public ResponseEntity<List<Account>> getAllAccounts() {
		
		return ResponseEntity.status(HttpStatus.OK).body(accountService.listAllAccounts());
	}
	
	@ApiOperation(value = "Retorna uma lista todas as contas cadastradas paginadas.")
	@GetMapping("/pageAccount")
	public ResponseEntity<Page<Account>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="3") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="id") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.status(HttpStatus.OK).body(accountService.findByPageAccount(page, linesPerPage, orderBy, direction));
	}
	
	@ApiOperation(value = "Retorna uma conta pelo id cadastrado.")
	@GetMapping("/{id}")
	public ResponseEntity<Account> findOneAccount(@PathVariable(value = "id") Long id) {
		
		Optional<Account> accountOpt = accountService.getById(id);		
		return ResponseEntity.status(HttpStatus.OK).body(accountOpt.get());
	}
	
	@ApiOperation(value = "Realiza um deposito em uma conta cadastrada")
	@PutMapping("/deposit/{id}")
	public ResponseEntity<Account> makeDeposit(@PathVariable(value = "id") Long id, @RequestBody MonetaryTransactionDto monetaryTransactionDto) {
		
		Optional<Account> accountOpt = accountService.getById(id);		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(accountService.doDeposit(accountOpt.get(), monetaryTransactionDto));
	}
	
	@ApiOperation(value = "Realiza um saque em uma conta cadastrada")
	@PutMapping("/withdrawal/{id}")
	public ResponseEntity<Account> makeWithdrawal(@PathVariable(value = "id") Long id, @RequestBody MonetaryTransactionDto monetaryTransactionDto) {
		
		Optional<Account> accountOpt = accountService.getById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(accountService.doWithdrawal(accountOpt.get(), monetaryTransactionDto));
	}
	
	@ApiOperation(value = "Retorna uma lista do histórico de transações de uma conta cadastrada paginada.")
	@GetMapping("/pageTransaction/{idUser}")
	public ResponseEntity<Page<MonetaryTransaction>> hystoricTransaction(
			@PathVariable(value = "idUser") Long idUser,
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value = "linesPage", defaultValue = "3") Integer linesPage,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		return ResponseEntity.status(HttpStatus.OK).body(monetaryTransactionService.findByPageMonetaryTransaction(idUser, page, linesPage, orderBy, direction));
	}
}
