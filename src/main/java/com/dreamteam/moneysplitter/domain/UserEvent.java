package com.dreamteam.moneysplitter.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserEvent {
    @EmbeddedId
    private UserEventKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "general_contrib")
    private Long GeneralContribution;
}
