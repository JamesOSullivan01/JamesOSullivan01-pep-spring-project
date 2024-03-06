package com.example.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Account;


public interface AccountRepository extends JpaRepository<Account, Long>{
    Account findByUsername(String username);
        
    //Query annotaion
    //JPQL
    @Query("SELECT a FROM Account a WHERE a.account_id = :account_id")  
    Account findByaccount_id (Integer account_id);



}

