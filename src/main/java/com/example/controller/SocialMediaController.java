package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

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
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
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

// ## 3: Our API should be able to process the creation of new messages.

// As a user, I should be able to submit a new post on the endpoint POST localhost:8080/messages. 
// The request body will contain a JSON representation of a message, which should be persisted to 
// the database, but will not contain a message_id.

// - The creation of the message will be successful if and only if the message_text is not blank, 
// is not over 255 characters, and posted_by refers to a real, existing user. If successful, 
// the response body should contain a JSON of the message, including its message_id. 
// The response status should be 200, which is the default. The new message should be persisted to the database.
// - If the creation of the message is not successful, the response status should be 400. (Client error)

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (isValidMessage(message)) {
            Account postedBy = accountService.findByaccount_id(message.getPosted_by());
            if (postedBy != null) {
                Message savedMessage = messageService.save(message);
                return ResponseEntity.ok(savedMessage);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    
    private boolean isValidMessage(Message message) {
        boolean messageIdValid = message.getMessage_id() == null;
        boolean messageTextValid = message.getMessage_text() != null 
                                 && !message.getMessage_text().isEmpty() 
                                 && message.getMessage_text().length() <= 255;
        boolean postedByValid = message.getPosted_by() != null;
        return messageIdValid && messageTextValid && postedByValid;
    }
    

}
