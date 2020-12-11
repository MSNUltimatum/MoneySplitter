package com.dreamteam.moneysplitter.domain;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

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

    private BigDecimal totalSpend = BigDecimal.ZERO;

    private String updatingDate;

    public UserStatistic(BigDecimal totalSpend, User user) {
        this.totalSpend = totalSpend;
    }

    public UserStatistic() {
    }
}
