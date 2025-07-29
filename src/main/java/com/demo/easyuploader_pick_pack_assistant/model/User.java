package com.demo.easyuploader_pick_pack_assistant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "PICKPACKER_USER")
public class User {
    @Id
    private Long id;
    private String login;
    private String authCode;
}
