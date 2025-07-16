package com.demo.easyuploader_pick_pack_assistant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "PICKPACKER_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String trackingNumber;
    private Boolean isCompleted;
    private LocalDateTime pickPackStartTime;
    private LocalDateTime completionTime;
    private String buyerLogin;
    private String orderNotes;

}
