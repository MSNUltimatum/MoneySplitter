package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.service.RelationshipsService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/getFriends")
    public ResponseEntity<Object> getMyFriends() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(principal),
                linkTo(methodOn(UserProfileController.class).myProfile()).withRel("profile")));
    }

    @PostMapping("/addToFriend/{friend_id}")
    public ResponseEntity<Object> addToFriends(@PathVariable("friend_id") Long friendId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        relationshipsService.sendFriendshipRequest(principal, friendId);
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(principal),
                linkTo(methodOn(UserProfileController.class).myProfile()).withRel("profile")));
    }

    @PostMapping("/deleteFromFriends/{friend_id}")
    public ResponseEntity<Object> deleteFriend(@PathVariable("friend_id") Long friendId) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        relationshipsService.deleteFromFriend(principal, friendId);
        return ResponseEntity.ok(CollectionModel.of(relationshipsService.getUserFriends(principal),
                linkTo(methodOn(UserProfileController.class).myProfile()).withRel("profile")));
    }
}
