package com.example.controller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;
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
    private MessageRepository messageRepository;

    @Autowired
    private final AccountService accountService;

    @Autowired
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService, MessageRepository messageRepository) {
        this.accountService = accountService;
        this.messageService = messageService;
        this.messageRepository = messageRepository;
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


    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(messages);
        }
    }


// ## 5: Our API should be able to retrieve a message by its ID.

// As a user, I should be able to submit a GET request on the endpoint 
// GET localhost:8080/messages/{message_id}.

// - The response body should contain a JSON representation of the message 
// identified by the message_id. It is expected for the response body to simply be 
// empty if there is no such message. The response status should always be 200, which is the default.


@GetMapping("/messages/{message_id}")
public ResponseEntity<Message> getMessageById(@PathVariable("message_id") Integer messageId) {
    Message message = messageRepository.findBymessage_id(messageId);
    if (message != null) {
        return ResponseEntity.ok(message);
    } else {
        return ResponseEntity.ok(null);
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
