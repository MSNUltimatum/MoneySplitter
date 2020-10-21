package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.Repositories.UserStatisticRepo;
import com.dreamteam.moneysplitter.assemblers.UserResourceAssembler;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/myProfile")
public class MainPageController {
    private final UserStatisticRepo userStatisticRepo;
    private final UserRepo userRepo;
    private final UserResourceAssembler userResourceAssembler;

    @Autowired
    public MainPageController(UserStatisticRepo userStatisticRepo, UserRepo userRepo, UserResourceAssembler userResourceAssembler) {
        this.userStatisticRepo = userStatisticRepo;
        this.userRepo = userRepo;
        this.userResourceAssembler = userResourceAssembler;
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> myProfile(@PathVariable("id") Long user_id) throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        User user = userRepo.findById(user_id).orElseThrow();
        EntityModel<User> value = userResourceAssembler.toModel(user);
        map.put("user", value);
        map.put("spend", userStatisticRepo.findByUser(user));
        MappingJacksonValue wrapper = new MappingJacksonValue(map);

        wrapper.setFilters(new SimpleFilterProvider()
                .addFilter("userFilter",
                        SimpleBeanPropertyFilter.serializeAllExcept(Set.of("id", "password")))
                .addFilter("statisticFilter",  SimpleBeanPropertyFilter.serializeAllExcept(Set.of("user"))));
        return ResponseEntity.ok().body(wrapper);
    }
}
