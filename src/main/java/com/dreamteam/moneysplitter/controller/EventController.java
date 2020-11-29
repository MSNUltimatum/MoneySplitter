package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.EventDTO;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import com.dreamteam.moneysplitter.domain.Event;
import com.dreamteam.moneysplitter.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable("id") Long eventId){
        Map<String, Object> eventById = eventService.getEventById(eventId);
        return ResponseEntity.ok(eventById);
    }

    @GetMapping("myEvents")
    public ResponseEntity<Object> getMyEvents(){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntityModel<EventDTO>> allUserEvents = eventService.getAllUserEvents(principal);
        return ResponseEntity.ok(allUserEvents);
    }
}
