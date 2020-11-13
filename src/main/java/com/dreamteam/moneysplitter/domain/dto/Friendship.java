package com.dreamteam.moneysplitter.domain.dto;

import com.dreamteam.moneysplitter.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friendship {
    private User sourceUser;
    private UserDTO destinationUser;
}
