package com.dreamteam.moneysplitter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Long eventId;
    private String dateOpen;
    private String dateClosed;
    private UserDTO owner;
    private String name;

    public EventDTO(String name) {
        this.name = name;
    }
}
