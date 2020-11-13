package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.FriendshipRequest;
import com.dreamteam.moneysplitter.service.RelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/{id}/getAllUserRequests")
    public ResponseEntity<Object> getAllUserFriendshipRequests(@PathVariable("id") Long userId){
        Set<EntityModel<FriendshipRequest>> allFriendshipRequests = relationshipsService.getAllFriendshipRequests(userId);
        return ResponseEntity.ok(allFriendshipRequests);
    }

    @GetMapping("/{id}/getFriendshipRequest")
    public ResponseEntity<Object> getOneFriendshipRequest(@PathVariable("id") Long requestId){
        EntityModel<FriendshipRequest> request = relationshipsService.getFriendshipRequest(requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{user_id}/applyRequest/{id}")
    public ResponseEntity<Object> applyRequest(@PathVariable("user_id") Long userId, @PathVariable("id") Long requestId){
        try {
            relationshipsService.applyRequest(requestId, userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(userId),
                linkTo(methodOn(UserProfileController.class).myProfile(userId)).withRel("profile")));
    }

    @PostMapping("/{user_id}/rejectRequest/{id}")
    public ResponseEntity<Object> rejectRequest(@PathVariable("user_id") Long userId,
                                                @PathVariable("id") Long requestId){
        try {
            relationshipsService.rejectRequest(requestId, userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(userId),
                linkTo(methodOn(UserProfileController.class).myProfile(userId)).withRel("profile")));
    }
}
