package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    @Query("SELECT m FROM Message m WHERE m.message_id = :message_id")
    Message findBymessage_id(Integer message_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Message m WHERE m.message_id = :message_id")
    void deleteBymessage_id (Integer message_id);

    @Transactional
    @Modifying
    @Query("UPDATE Message m SET m.message_text = :message_text WHERE m.message_id = :message_id")
    void updateMessagePlease(@Param("message_id") Integer message_id, @Param("message_text") String message_text);

    @Query("SELECT m FROM Message m WHERE m.posted_by = :account_id")
    List<Message> findByPostedBy(Integer account_id);
}
