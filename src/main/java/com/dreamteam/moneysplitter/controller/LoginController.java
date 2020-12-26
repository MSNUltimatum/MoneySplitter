package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.assemblers.UserResourceAssembler;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/baseApi")
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/addUser")
    public ResponseEntity<Object> addUser(@RequestBody User user) throws URISyntaxException {
        User user1 = userService.createUser(user);
        if(user1 == null){
            return ResponseEntity.badRequest().body("User exist");
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
