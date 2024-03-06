package com.example.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    @Query("SELECT m FROM Message m WHERE m.message_id = :message_id")
    Message findBymessage_id(Integer message_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Message m WHERE m.message_id = :message_id")
    void deleteBymessage_id (Integer message_id);
   
}
