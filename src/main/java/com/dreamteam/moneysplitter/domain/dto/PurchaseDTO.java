package com.dreamteam.moneysplitter.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {
    private UserDTO user;
    private String purchaseName;
    private BigDecimal purchaseCost;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private String date;

    public PurchaseDTO(String purchaseName, BigDecimal purchaseCost) {
        this.purchaseName = purchaseName;
        this.purchaseCost = purchaseCost;
    }
}
