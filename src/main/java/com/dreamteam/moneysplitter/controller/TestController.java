package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.Event;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.Repositories.EventRepo;
import com.dreamteam.moneysplitter.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    private final UserRepo userRepo;
    private final EventRepo eventRepo;

    @Autowired
    public TestController(UserRepo userRepos, EventRepo eventRepo) {
        this.userRepo = userRepos;
        this.eventRepo = eventRepo;
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userRepo.findAll();
    }

    @PostMapping("/users")
    public void addUser(@RequestBody User user){
        userRepo.save(user);
        System.out.println(user);
    }

    @PostMapping("/{id}/events")
    public void addEvent(@PathVariable("id") User user,@RequestBody Event event){
        event.setOwner(user);
        eventRepo.save(event);
    }
}
