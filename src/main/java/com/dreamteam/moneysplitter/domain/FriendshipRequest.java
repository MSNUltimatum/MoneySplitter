package com.dreamteam.moneysplitter.domain;

import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"source_id", "destination_id"}))
@NoArgsConstructor
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;

    @OneToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id")
    private User sourceUser;

    @OneToOne
    @JoinColumn(name = "destination_id", referencedColumnName = "id")
    private User destinationUser;

    public FriendshipRequest(User sourceUser, User destinationUser) {
        this.sourceUser = sourceUser;
        this.destinationUser = destinationUser;
    }
}
