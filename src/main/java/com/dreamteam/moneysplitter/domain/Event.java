package com.dreamteam.moneysplitter.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "EVENT")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dateOpen;
    private String dateClosed;
    private String name;
    private Boolean isClosed;

    @OneToOne
    @MapsId
    private User owner;
}
