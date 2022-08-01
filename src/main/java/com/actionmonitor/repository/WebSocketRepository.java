package com.actionmonitor.repository;


import com.actionmonitor.model.MessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebSocketRepository extends JpaRepository<MessageHistory, Integer> {
}
