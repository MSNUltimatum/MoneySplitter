package com.dreamteam.moneysplitter.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;

@Entity
@Data
@JsonFilter("statisticFilter")
public class UserStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @JsonView(Views.UserProp.class)
    private Long totalSpend;

    @JsonView(Views.UserProp.class)
    private String updatingDate;
}
