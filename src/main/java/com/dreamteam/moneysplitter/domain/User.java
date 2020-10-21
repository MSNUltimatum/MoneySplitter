package com.dreamteam.moneysplitter.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_table")
@Data
@JsonFilter("userFilter")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView(Views.UserProp.class)
    private String email;
    private String password;
    @JsonView(Views.UserProp.class)
    private String firstName;
    @JsonView(Views.UserProp.class)
    private String secondName;
}
