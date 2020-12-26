package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.assemblers.EventPurchaseResourceAssembler;
import com.dreamteam.moneysplitter.assemblers.EventResourceAssembler;
import com.dreamteam.moneysplitter.assemblers.OtherUserResourceAssembler;
import com.dreamteam.moneysplitter.controller.EventController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.*;
import com.dreamteam.moneysplitter.domain.dto.EventDTO;
import com.dreamteam.moneysplitter.domain.dto.MyPartDTO;
import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.repositories.EventRepo;
import com.dreamteam.moneysplitter.repositories.PurchaseRepo;
import com.dreamteam.moneysplitter.repositories.UserEventRepo;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EventService {
    private final EventRepo eventRepo;
    private final UserEventRepo userEventRepo;
    private final UserRepo userRepo;
    private final PurchaseRepo purchaseRepo;
    private final OtherUserResourceAssembler friendResourceAssembler;
    private final EventResourceAssembler eventResourceAssembler;
    private final EventPurchaseResourceAssembler purchaseResourceAssembler;

    @Autowired
    public EventService(EventRepo eventRepo,
                        UserEventRepo userEventRepo,
                        UserRepo userRepo,
                        PurchaseRepo purchaseRepo, OtherUserResourceAssembler friendResourceAssembler,
                        EventResourceAssembler eventResourceAssembler, EventPurchaseResourceAssembler purchaseResourceAssembler) {
        this.eventRepo = eventRepo;
        this.userEventRepo = userEventRepo;
        this.userRepo = userRepo;
        this.purchaseRepo = purchaseRepo;
        this.friendResourceAssembler = friendResourceAssembler;
        this.eventResourceAssembler = eventResourceAssembler;
        this.purchaseResourceAssembler = purchaseResourceAssembler;
    }

    @Transactional
    public Map<String, Object> getEventById(Long eventId, String principal) {
        User user = userRepo.findByEmail(principal);
        Event event = eventRepo.findById(eventId).orElseThrow();
        return constructEvent(user, event);
    }

    private Map<String, Object> constructEvent(User user, Event event) {
        Map<String, Object> map = new HashMap<>();
        EventDTO eventDTO = DTOMaker.getEventDTO(event);
        Set<EntityModel<UserDTO>> collect = getUserDTOEntityModels(user, event);
        Set<EntityModel<PurchaseDTO>> purchaseDTOS = getPurchaseDTOEntityModels(user, event);
        map.put("event", eventResourceAssembler.toModel(eventDTO));
        map.put("users", collect);
        map.put("purchases", purchaseDTOS);
        return map;
    }

    private Set<EntityModel<PurchaseDTO>> getPurchaseDTOEntityModels(User user, Event event) {
        return purchaseRepo.findAllByEvent(event).stream().map(e -> {
            EntityModel<PurchaseDTO> purchaseDTOEntityModel = purchaseResourceAssembler.toModel(DTOMaker.getPurchaseDTO(e));
            if (e.getUser().equals(user)) {
                purchaseDTOEntityModel.add(linkTo(methodOn(EventController.class).deletePurchaseFromEvent(event.getId(), e.getId()))
                        .withRel("DeletePurchase"));
            }
            return purchaseDTOEntityModel;
        }).collect(Collectors.toSet());
    }

    private Set<EntityModel<UserDTO>> getUserDTOEntityModels(User user, Event event) {
        List<UserEvent> allByEvent = userEventRepo.findAllByEvent(event);
        return allByEvent.stream().map(e -> {
            EntityModel<UserDTO> userDTOEntityModel = friendResourceAssembler.toModel(DTOMaker.getUserDTO(e.getUser()));
            if (event.getOwner().equals(user)) {
                userDTOEntityModel.add(linkTo(methodOn(EventController.class).deleteUserFromEvent(e.getUser().getId(), event.getId()))
                        .withRel("DeletePerson"));
            }
            return userDTOEntityModel;
        }).collect(Collectors.toSet());
    }

    @Transactional
    public List<EntityModel<EventDTO>> getAllUserEvents(String principal) {
        return userEventRepo.findAllByUser(userRepo.findByEmail(principal))
                .stream()
                .map(e -> eventResourceAssembler.toModel(DTOMaker.getEventDTO(e.getEvent())))
                .collect(Collectors.toList());
    }

    @Transactional
    public EntityModel<EventDTO> createEvent(EventDTO eventDTO, String principal) {
        User user = userRepo.findByEmail(principal);
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDateOpen(LocalDate.now().toString());
        event.setOwner(user);
        event.setIsClosed(false);
        Event save = eventRepo.save(event);
        addUserOwnToEvent(user, save);
        return eventResourceAssembler.toModel(DTOMaker.getEventDTO(save));
    }

    @Transactional
    public Boolean addUserToEvent(Long eventId, Long userId, String principal) {
        User invitedUser = userRepo.findById(userId).orElseThrow();
        User inviter = userRepo.findByEmail(principal);
        Event event = eventRepo.findById(eventId).orElseThrow();
        if (!event.getIsClosed() && isEventParticipant(inviter, event) &&
                userEventRepo.findAllByEvent(event).stream().noneMatch(e -> e.getUser().equals(invitedUser))) {
            addUserOwnToEvent(invitedUser, event);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteUserFromEvent(Long eventId, Long userId, String principal) {
        User deletedUser = userRepo.findById(userId).orElseThrow();
        User owner = userRepo.findByEmail(principal);
        Event event = eventRepo.findById(eventId).orElseThrow();
        if (!event.getIsClosed() && event.getOwner().equals(owner) && isEventParticipant(deletedUser, event)) {
            Optional<UserEvent> first = userEventRepo.findAllByEvent(event).stream().filter(e -> e.getUser().equals(deletedUser)).findFirst();
            if (first.isPresent()) {
                userEventRepo.delete(first.get());
                return true;
            }
        }
        return false;
    }

    private void addUserOwnToEvent(User user, Event event) {
        UserEvent userEvent = new UserEvent();
        userEvent.setUser(user);
        userEvent.setEvent(event);
        userEvent.setGeneralContribution(BigDecimal.ZERO);
        userEvent.setId(new UserEventKey(user.getId(), event.getId()));
        userEventRepo.save(userEvent);
    }

    @Transactional
    public boolean addPurchaseToEvent(Long eventId, PurchaseDTO purchaseDTO, String principal) {
        User user = userRepo.findByEmail(principal);
        Event event = eventRepo.findById(eventId).orElseThrow();
        if (!event.getIsClosed() && isEventParticipant(user, event)) {
            Purchase purchase = new Purchase();
            purchase.setDate(LocalDate.now().toString());
            purchase.setEvent(event);
            purchase.setUser(user);
            purchase.setPurchaseCost(purchaseDTO.getPurchaseCost());
            purchase.setPurchaseName(purchaseDTO.getPurchaseName());
            purchaseRepo.save(purchase);
            return true;
        }
        return false;
    }

    private boolean isEventParticipant(User user, Event event) {
        return userEventRepo.findAllByEvent(event).stream().anyMatch(e -> e.getUser().equals(user));
    }

    @Transactional
    public boolean deletePurchaseFromEvent(Long eventId, Long purchaseId, String principal) {
        User user = userRepo.findByEmail(principal);
        Event event = eventRepo.findById(eventId).orElseThrow();
        Purchase purchase = purchaseRepo.findById(purchaseId).orElseThrow();
        if (!event.getIsClosed() && purchase.getEvent() != null && purchase.getEvent().equals(event) && purchase.getUser().equals(user)) {
            purchaseRepo.delete(purchase);
            return true;
        }
        return false;
    }

    @Transactional
    public EntityModel<MyPartDTO> getMyPart(String principal, Long eventId) {
        Event event = eventRepo.findById(eventId).orElse(null);
        User user = userRepo.findByEmail(principal);
        if(event != null && !event.getIsClosed() && isEventParticipant(user, event)){
            List<Purchase> allPurchasesByEvent = purchaseRepo.findAllByEvent(event);
            BigDecimal allCost = allPurchasesByEvent.stream().map(Purchase::getPurchaseCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal myPart = allCost.divide(BigDecimal.valueOf(userEventRepo.findAllByEvent(event).size()));
            return EntityModel.of(new MyPartDTO(myPart),
                    linkTo(methodOn(UserProfileController.class).myProfile()).withRel("myAccount"),
                    linkTo(methodOn(EventController.class).getMyEvents()).withRel("myEvents"),
                    linkTo(methodOn(EventController.class).getEventById(eventId)).withRel("thisEvent"),
                    linkTo(methodOn(EventController.class).split(eventId)).withSelfRel());
        }
        return null;
    }

    @Transactional
    public boolean closeEvent(String principal, Long eventId) {
        Event event = eventRepo.findById(eventId).orElse(null);
        User user = userRepo.findByEmail(principal);
        if(event != null && event.getOwner().equals(user)){
            event.setIsClosed(true);
            event.setDateClosed(LocalDate.now().toString());
            eventRepo.save(event);
            return true;
        }
        return false;
    }
}
