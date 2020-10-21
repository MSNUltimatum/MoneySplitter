package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.Repositories.PurchaseRepo;
import com.dreamteam.moneysplitter.domain.Event;
import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.Repositories.EventRepo;
import com.dreamteam.moneysplitter.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    private final UserRepo userRepo;
    private final EventRepo eventRepo;
    private final PurchaseRepo purchaseRepo;

    @Autowired
    public TestController(UserRepo userRepos, EventRepo eventRepo, PurchaseRepo purchaseRepo) {
        this.userRepo = userRepos;
        this.eventRepo = eventRepo;
        this.purchaseRepo = purchaseRepo;
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

    @PostMapping("/{id}/soloPurchase")
    public void addPurchase(@PathVariable("id") User user,@RequestBody Purchase purchase){
        purchase.setUser(user);
        purchaseRepo.save(purchase);
    }

    @GetMapping("/{id}/userPurchases")
    public ResponseEntity<List<Purchase>> userPurchases(@PathVariable("id") User user){
        return new ResponseEntity<>(new ArrayList<>(purchaseRepo.findAllByUser(user)), HttpStatus.NOT_FOUND);
    }

}
