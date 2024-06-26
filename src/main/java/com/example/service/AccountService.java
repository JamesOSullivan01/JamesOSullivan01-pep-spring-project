package com.example.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
@Service
public class AccountService {

   
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account save(Account account) {
        return accountRepository.save(account); 
}


    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account findByaccount_id(Integer account_id) {
        return accountRepository.findByaccount_id(account_id);
    }    
    
}
