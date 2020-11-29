package com.dreamteam.moneysplitter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventDTO {
    private Long eventId;
    private String dateOpen;
    private String dateClosed;
    private String name;
}
