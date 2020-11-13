package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.repositories.UserEventRepo;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import com.dreamteam.moneysplitter.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/event")
public class EventController {
    private final UserEventRepo eventRepo;
    private final UserRepo userRepo;

    @Autowired
    public EventController(UserEventRepo eventRepo, UserRepo userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionModel<EntityModel<Event>>> getMyEvents(@PathVariable("id") Long user_id){
        List<EntityModel<Event>> collect = eventRepo.findAllByUser(userRepo.findById(user_id).orElseThrow())
                .stream()
                .map(e -> EntityModel.of(e.getEvent()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(CollectionModel.of(collect));
    }
}
