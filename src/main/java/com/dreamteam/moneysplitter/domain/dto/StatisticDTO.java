package com.dreamteam.moneysplitter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class StatisticDTO {
    private BigDecimal totalSpend;
}
