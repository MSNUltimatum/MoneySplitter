package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.FriendshipRequest;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.FriendshipRequestDTO;
import com.dreamteam.moneysplitter.service.RelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/friendshipRequests")
public class FriendshipRequestsController {
    private final RelationshipsService relationshipsService;

    @Autowired
    public FriendshipRequestsController(RelationshipsService relationshipsService) {
        this.relationshipsService = relationshipsService;
    }


    @GetMapping("/getAllUserRequests")
    public ResponseEntity<Object> getAllUserFriendshipRequests() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<EntityModel<FriendshipRequestDTO>> allFriendshipRequests = relationshipsService.getAllFriendshipRequests(principal);
        return ResponseEntity.ok(allFriendshipRequests);
    }

    @GetMapping("/{id}/getFriendshipRequest")
    public ResponseEntity<Object> getOneFriendshipRequest(@PathVariable("id") Long requestId) {
        EntityModel<FriendshipRequest> request = relationshipsService.getFriendshipRequest(requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/applyRequest/{id}")
    public ResponseEntity<Object> applyRequest(@PathVariable("id") Long requestId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            relationshipsService.applyRequest(requestId, principal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(principal),
                linkTo(methodOn(UserProfileController.class).myProfile()).withRel("profile")));
    }

    @PostMapping("/rejectRequest/{id}")
    public ResponseEntity<Object> rejectRequest(@PathVariable("id") Long requestId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            relationshipsService.rejectRequest(requestId, principal);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(principal),
                linkTo(methodOn(UserProfileController.class).myProfile()).withRel("profile")));
    }
}
