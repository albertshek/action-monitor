package com.actionmonitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MessageHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(updatable = false)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String userFrom;

    private String userTo;

    private Timestamp timestamp;
}
