package com.example.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable("message_id") Integer messageId) {
    Message message = messageRepository.findBymessage_id(messageId);
    if (message != null) {
        return ResponseEntity.ok(message);
    } else {
        return ResponseEntity.ok(null);
    }
}

@DeleteMapping("/messages/{message_id}")
public ResponseEntity<Object> deleteMessageById(@PathVariable("message_id") Integer messageId) {
    if (messageRepository.findBymessage_id(messageId) != null) {
        messageRepository.deleteBymessage_id(messageId);
        return ResponseEntity.ok().body(1);
    } else {
        return ResponseEntity.ok().build();
    }
}

// ## 7: Our API should be able to update a message text identified by a message ID.

// As a user, I should be able to submit a PATCH request on the endpoint PATCH 
// localhost:8080/messages/{message_id}. The request body should contain a new message_text 
// values to replace the message identified by message_id. The request body can not be 
// guaranteed to contain any other information.

// - The update of a message should be successful if and only if the message id already 
// exists and the new message_text is not blank and is not over 255 characters. If the 
// update is successful, the response body should contain the number of rows updated (1), 
// and the response status should be 200, which is the default. The message existing on 
// the database should have the updated message_text.
// - If the update of the message is not successful for any reason, the response status 
// should be 400. (Client error)

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Object> updateMessageById(@PathVariable("message_id") Integer message_id, @RequestBody String message_text) {
    System.out.println("******Received message text: " + message_text); 

    Message messageToUpdate = messageRepository.findBymessage_id(message_id);
    if (message_text == null || message_text.trim().isEmpty() || message_text.equals("")) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (message_text.length() > 255){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
    if (messageToUpdate != null && isValidMessage(messageToUpdate)) {
        System.out.println("*|*|*|*|*|*|*|**Message is valid."); 
        if (message_text.isEmpty()) {
            System.out.println("****************Message text is empty."); 

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        messageRepository.updateMessagePlease(message_id, message_text);
        return ResponseEntity.ok(1);
    } else {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
    
    
    private boolean isValidMessage(Message message) {
        boolean messageIdValid = message.getMessage_id() != null;
        boolean messageTextValid = message.getMessage_text() != null 
                                 && !message.getMessage_text().equals("") 
                                 && !message.getMessage_text().isEmpty() 
                                 && message.getMessage_text().length() < 255;
        boolean postedByValid = message.getPosted_by() != null;
        return messageIdValid && messageTextValid && postedByValid;
    }



    
    

}
