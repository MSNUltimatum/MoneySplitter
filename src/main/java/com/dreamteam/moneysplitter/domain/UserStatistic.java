package com.dreamteam.moneysplitter.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @MapsId
    private User user;
    private Long totalSpend;
    private String updatingDate;
}
