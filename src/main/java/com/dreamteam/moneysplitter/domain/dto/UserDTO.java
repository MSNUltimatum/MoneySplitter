package com.dreamteam.moneysplitter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String secondName;
    private String email;
}
