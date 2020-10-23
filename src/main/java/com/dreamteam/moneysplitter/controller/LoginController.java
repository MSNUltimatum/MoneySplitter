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
    private final UserResourceAssembler resourceAssembler;

    @Autowired
    public LoginController(UserService userService, UserResourceAssembler resourceAssembler) {
        this.userService = userService;
        this.resourceAssembler = resourceAssembler;
    }

    @PostMapping("/addUser")
    public ResponseEntity<EntityModel<User>> addUser(@RequestBody User user) throws URISyntaxException { //TODO фильтровать выдаваемый ответ
        EntityModel<User> resource = resourceAssembler.toModel(userService.createUser(user));
        ResponseEntity<EntityModel<User>> body = ResponseEntity.created(new URI(resource.getLink("self").get().expand().getHref())).body(resource);
        return body;
    }
}
