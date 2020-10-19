package com.dreamteam.moneysplitter.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_table")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;
    private String password;
    private String firstName;
    private String secondName;
}
