package com.demo.easyuploader_pick_pack_assistant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String token;
    @JsonIgnore
    private String userRole;
}
