package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.domain.dto.StatisticDTO;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import com.dreamteam.moneysplitter.repositories.UserStatisticRepo;
import com.dreamteam.moneysplitter.assemblers.UserResourceAssembler;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserRoles;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final StatisticService statisticService;
    private final UserStatisticRepo statisticRepo;
    private final UserResourceAssembler userResourceAssembler;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepo userRepo,
                       StatisticService statisticService, UserStatisticRepo statisticRepo,
                       UserResourceAssembler userResourceAssembler,
                       PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.statisticService = statisticService;
        this.statisticRepo = statisticRepo;
        this.userResourceAssembler = userResourceAssembler;
        this.passwordEncoder = passwordEncoder;
    }

    public EntityModel<UserDTO> getUserProfile(String principal){
        User entity = userRepo.findByEmail(principal);
        return getUserEntityModel(entity);
    }

    public EntityModel<UserDTO> getUserProfile(Long userId){
        User entity = userRepo.findById(userId).orElseThrow();
        return getUserEntityModel(entity);
    }

    private EntityModel<UserDTO> getUserEntityModel(User entity) {
        if(entity == null)
            throw new IllegalStateException("Empty principal");
        return userResourceAssembler.toModel(new UserDTO(entity.getId(),
                entity.getFirstName(),
                entity.getSecondName(),
                entity.getEmail()));
    }

    public User createUser(User user){
        user.setRoles(Sets.newHashSet(UserRoles.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        UserStatistic userStatistic = new UserStatistic();
        userStatistic.setTotalSpend(BigDecimal.ZERO);
        userStatistic.setUpdatingDate(LocalDate.now().toString());
        userStatistic.setUser(user);
        statisticRepo.save(userStatistic);
        return userRepo.save(user);
    }
}
