package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.dto.EventDTO;
import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import com.dreamteam.moneysplitter.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEventById(@PathVariable("id") Long eventId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> eventById = eventService.getEventById(eventId, principal);
        return ResponseEntity.ok(eventById);
    }

    @GetMapping("myEvents")
    public ResponseEntity<Object> getMyEvents() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntityModel<EventDTO>> allUserEvents = eventService.getAllUserEvents(principal);
        return ResponseEntity.ok(CollectionModel.of(allUserEvents,
                linkTo(methodOn(EventController.class).getMyEvents()).withSelfRel()));
    }

    @PostMapping("/createEvent")
    public ResponseEntity<Object> createEvent(@RequestBody(required = false) EventDTO eventDTO) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EntityModel<EventDTO> event = eventService.createEvent(eventDTO, principal);
        return ResponseEntity.ok(event);
    }

    @PostMapping("/addUserToEvent/{user_id}/{event_id}")
    public ResponseEntity<Object> addUserToEvent(@PathVariable("user_id") Long userId, @PathVariable("event_id") Long eventId){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(eventService.addUserToEvent(eventId, userId, principal)) {
            return ResponseEntity.ok(eventService.getEventById(eventId, principal));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteUserFromEvent/{user_id}/{event_id}")
    public ResponseEntity<Object> deleteUserFromEvent(@PathVariable("user_id") Long userId, @PathVariable("event_id") Long eventId){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(eventService.deleteUserFromEvent(eventId, userId, principal)) {
            return ResponseEntity.ok(eventService.getEventById(eventId, principal));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/addPurchaseToEvent/{event_id}")
    public ResponseEntity<Object> addPurchaseToEvent(@PathVariable("event_id") Long eventId,
                                                     @RequestBody(required = false)PurchaseDTO purchaseDTO){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(eventService.addPurchaseToEvent(eventId, purchaseDTO, principal)){
            return ResponseEntity.ok(eventService.getEventById(eventId, principal));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deletePurchaseFromEvent/{event_id}/{purchase_id}")
    public ResponseEntity<Object> deletePurchaseFromEvent(@PathVariable("event_id") Long eventId,
                                                          @PathVariable("purchase_id") Long purchaseId){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(eventService.deletePurchaseFromEvent(eventId, purchaseId, principal)){
            return ResponseEntity.ok(eventService.getEventById(eventId, principal));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


}
