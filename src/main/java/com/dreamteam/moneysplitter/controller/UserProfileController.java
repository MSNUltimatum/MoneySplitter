package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.dto.StatisticDTO;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.service.RelationshipsService;
import com.dreamteam.moneysplitter.service.StatisticService;
import com.dreamteam.moneysplitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/myProfile")
public class UserProfileController {
    private final UserService userService;
    private final StatisticService statisticService;
    private final RelationshipsService relationshipsService;

    @Autowired
    public UserProfileController(UserService userService, StatisticService statisticService, RelationshipsService relationshipsService) {
        this.userService = userService;
        this.statisticService = statisticService;
        this.relationshipsService = relationshipsService;
    }

    @GetMapping
    public ResponseEntity<Object> myProfile() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EntityModel<UserDTO> user = userService.getUserProfile(principal);
        EntityModel<StatisticDTO> monthStatistic = statisticService.getMonthStatistic(principal);
        Set<EntityModel<UserDTO>> userFriends = relationshipsService.getUserFriends(principal);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("statistic", monthStatistic);
        map.put("friends", userFriends);
        return ResponseEntity.ok().body(map);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> otherUser(@PathVariable("id") Long userId) {
        EntityModel<UserDTO> user = userService.getUserProfile(userId);
        Set<EntityModel<UserDTO>> userFriends = relationshipsService.getUserFriends(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("friends", userFriends);
        return ResponseEntity.ok().body(map);
    }
}
