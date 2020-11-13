package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.service.RelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/relationships/")
public class RelationshipsController {

    private final RelationshipsService relationshipsService;

    @Autowired
    public RelationshipsController(RelationshipsService relationshipsService) {
        this.relationshipsService = relationshipsService;
    }

    @GetMapping("/{id}/getFriends")
    public ResponseEntity<Object> getMyFriends(@PathVariable("id") Long user_id){
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(user_id),
                linkTo(methodOn(UserProfileController.class).myProfile(user_id)).withRel("profile")));
    }

    @PostMapping("/{user_id}/addToFriend/{friend_id}")
    public ResponseEntity<Object> addToFriends(@PathVariable("user_id") Long userId,
                                               @PathVariable("friend_id") Long friendId){
        if(!userId.equals(friendId)){
            relationshipsService.sendFriendshipRequest(userId, friendId);
        }
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(userId),
                linkTo(methodOn(UserProfileController.class).myProfile(userId)).withRel("profile")));
    }

    @PostMapping("/{user_id}/deleteFromFriends/{friend_id}")
    public ResponseEntity<Object> deleteFriend(@PathVariable("user_id") Long userId,
                                               @PathVariable("friend_id") Long friendId){
        if(!userId.equals(friendId)){
            relationshipsService.deleteFromFriend(userId, friendId);
        }
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(userId),
                linkTo(methodOn(UserProfileController.class).myProfile(userId)).withRel("profile")));
    }
}
