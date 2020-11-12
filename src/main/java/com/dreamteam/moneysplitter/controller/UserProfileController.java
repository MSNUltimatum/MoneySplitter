package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.service.StatisticService;
import com.dreamteam.moneysplitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/myProfile")
public class UserProfileController {
    private final UserService userService;

    @Autowired
    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> myProfile(@PathVariable("id") Long user_id) {
        Map<String, Object> profile = userService.getUserProfile(user_id);
        return ResponseEntity.ok().body(profile);
    }

}
