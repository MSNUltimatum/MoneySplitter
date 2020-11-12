package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.service.RelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/relationships/")
public class RelationshipsController {

    private final RelationshipsService relationshipsService;

    @Autowired
    public RelationshipsController(RelationshipsService relationshipsService) {
        this.relationshipsService = relationshipsService;
    }

    @GetMapping("/getFriends/{id}")
    public ResponseEntity<Set<EntityModel<UserDTO>>> getMyFriends(@PathVariable("id") Long user_id){
        return ResponseEntity.ok(relationshipsService.getUserFriends(user_id));
    }
}
