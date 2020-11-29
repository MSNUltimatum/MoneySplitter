package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.assemblers.EventResourceAssembler;
import com.dreamteam.moneysplitter.assemblers.OtherUserResourceAssembler;
import com.dreamteam.moneysplitter.domain.Event;
import com.dreamteam.moneysplitter.domain.UserEvent;
import com.dreamteam.moneysplitter.domain.dto.EventDTO;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.repositories.EventRepo;
import com.dreamteam.moneysplitter.repositories.UserEventRepo;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepo eventRepo;
    private final UserEventRepo userEventRepo;
    private final UserRepo userRepo;
    private final OtherUserResourceAssembler friendResourceAssembler;
    private final EventResourceAssembler eventResourceAssembler;

    @Autowired
    public EventService(EventRepo eventRepo,
                        UserEventRepo userEventRepo,
                        UserRepo userRepo,
                        OtherUserResourceAssembler friendResourceAssembler,
                        EventResourceAssembler eventResourceAssembler) {
        this.eventRepo = eventRepo;
        this.userEventRepo = userEventRepo;
        this.userRepo = userRepo;
        this.friendResourceAssembler = friendResourceAssembler;
        this.eventResourceAssembler = eventResourceAssembler;
    }

    public Map<String, Object> getEventById(Long eventId) {
        Event event = eventRepo.findById(eventId).orElseThrow();
        List<UserEvent> allByEvent = userEventRepo.findAllByEvent(event);
        Map<String, Object> map = new HashMap<>();
        EventDTO eventDTO = DTOMaker.getEventDTO(event);
        Set<EntityModel<UserDTO>> collect = allByEvent.stream().map(e -> friendResourceAssembler.toModel(DTOMaker.getUserDTO(e.getUser()))).collect(Collectors.toSet());
        map.put("event", eventResourceAssembler.toModel(eventDTO));
        map.put("users", collect);
        return map;
    }

    public List<EntityModel<EventDTO>> getAllUserEvents(String principal) {
        List<EntityModel<EventDTO>> collect = userEventRepo.findAllByUser(userRepo.findByEmail(principal))
                .stream()
                .map(e -> eventResourceAssembler.toModel(DTOMaker.getEventDTO(e.getEvent())))
                .collect(Collectors.toList());
        return collect;
    }
}
