package com.dreamteam.moneysplitter.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PURCHASE")
@Data
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String purchaseName;
    private Long purchaseCost = 0L;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date date = new Date();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Event event;
}
