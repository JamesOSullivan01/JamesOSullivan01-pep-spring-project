package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    @Autowired
    private final AccountService accountService;

    @Autowired
    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerNewAccount(@RequestBody Account account) {
    if (account.getUsername() != null && account.getPassword().length() > 4) {
        Account existingAccount = accountService.findByUsername(account.getUsername());
        if (existingAccount == null) {
            Account savedAccount = accountService.save(account);
            return ResponseEntity.ok(savedAccount);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
    @PostMapping("/login")
    public ResponseEntity<Account> loginToApp(@RequestBody Account account) {
    Account existingAccount = accountService.findByUsername(account.getUsername());
    if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
        return ResponseEntity.ok(existingAccount);
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}


}
