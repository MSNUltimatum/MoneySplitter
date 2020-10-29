package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.Repositories.UserStatisticRepo;
import com.dreamteam.moneysplitter.assemblers.UserResourceAssembler;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserRoles;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
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
    private final JacksonSerializer jacksonSerializer;

    @Autowired
    public UserService(UserRepo userRepo,
                       StatisticService statisticService, UserStatisticRepo statisticRepo,
                       UserResourceAssembler userResourceAssembler,
                       PasswordEncoder passwordEncoder, JacksonSerializer jacksonSerializer) {
        this.userRepo = userRepo;
        this.statisticService = statisticService;
        this.statisticRepo = statisticRepo;
        this.userResourceAssembler = userResourceAssembler;
        this.passwordEncoder = passwordEncoder;
        this.jacksonSerializer = jacksonSerializer;
    }

    public MappingJacksonValue getUserProfile(Long user_id){
        EntityModel<User> user = getUserEntityModel(user_id);
        EntityModel<UserStatistic> statisticEntityModel = statisticService.getStatisticEntityModel(user_id);
        Map<String, Object> profile = new HashMap<>();
        profile.put("user", user);
        profile.put("statistic", statisticEntityModel);
        return jacksonSerializer.getFilteringJacksonValueUserStatistic(profile);
    }

    private EntityModel<User> getUserEntityModel(Long user_id) {
        User entity = userRepo.findById(user_id).orElseThrow();
        return userResourceAssembler.toModel(entity);
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
