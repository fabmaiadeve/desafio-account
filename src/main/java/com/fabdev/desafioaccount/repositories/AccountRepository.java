package com.fabdev.desafioaccount.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fabdev.desafioaccount.models.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
