package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.PurchaseRepo;
import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.Repositories.UserStatisticRepo;
import com.dreamteam.moneysplitter.assemblers.StatisticResourceAssembler;
import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StatisticService {

    private final StatisticResourceAssembler statisticResourceAssembler;
    private final UserStatisticRepo userStatisticRepo;
    private final PurchaseRepo purchaseRepo;
    private final UserRepo userRepo;
    private final JacksonSerializer jacksonSerializer;

    @Autowired
    public StatisticService(StatisticResourceAssembler statisticResourceAssembler, UserStatisticRepo userStatisticRepo, PurchaseRepo purchaseRepo, UserRepo userRepo, JacksonSerializer jacksonSerializer) {
        this.statisticResourceAssembler = statisticResourceAssembler;
        this.userStatisticRepo = userStatisticRepo;
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
        this.jacksonSerializer = jacksonSerializer;
    }

    public MappingJacksonValue getMonthStatistic(Long user_id){
        EntityModel<UserStatistic> statistic = getStatisticEntityModel(userRepo.findById(user_id).orElseThrow());
        return jacksonSerializer.getFilteringJacksonValueStatistic(statistic);
    }

    public void addToStatistic(Long user_id, BigDecimal cost){
        UserStatistic byUser = userStatisticRepo.findByUser(userRepo.findById(user_id).orElseThrow());
        byUser.setTotalSpend(byUser.getTotalSpend().add(cost));
        userStatisticRepo.save(byUser);
    }

    EntityModel<UserStatistic> getStatisticEntityModel(User user) {
        UserStatistic entity = userStatisticRepo.findByUser(user);
        return statisticResourceAssembler.toModel(entity);
    }

    EntityModel<UserStatistic> getStatisticEntityModel(Long user_id) {
        UserStatistic entity = userStatisticRepo.findByUser(userRepo.findById(user_id).orElseThrow());
        return statisticResourceAssembler.toModel(entity);
    }

    public Map<String, Object> getIntervalStatistic(Long user_id, LocalDate startDate, LocalDate endDate){
        User user = userRepo.findById(user_id).orElseThrow();
        Map<String, Object> map = new HashMap<>();
        Collection<Purchase> byDayInterval = purchaseRepo.findAllBetweenDates(startDate, endDate, user);
        map.put("purchases", byDayInterval);
        return map;
    }
}
