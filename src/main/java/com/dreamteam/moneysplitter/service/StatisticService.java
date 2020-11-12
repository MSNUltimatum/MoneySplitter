package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.PurchaseRepo;
import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.Repositories.UserStatisticRepo;
import com.dreamteam.moneysplitter.assemblers.StatisticResourceAssembler;
import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import com.dreamteam.moneysplitter.domain.dto.StatisticDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

@Service
public class StatisticService {

    private final StatisticResourceAssembler statisticResourceAssembler;
    private final UserStatisticRepo userStatisticRepo;
    private final PurchaseRepo purchaseRepo;
    private final UserRepo userRepo;

    @Autowired
    public StatisticService(StatisticResourceAssembler statisticResourceAssembler,
                            UserStatisticRepo userStatisticRepo,
                            PurchaseRepo purchaseRepo,
                            UserRepo userRepo) {
        this.statisticResourceAssembler = statisticResourceAssembler;
        this.userStatisticRepo = userStatisticRepo;
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
    }

    public EntityModel<StatisticDTO> getMonthStatistic(Long user_id){
        return getStatisticEntityModel(userRepo.findById(user_id).orElseThrow());
    }

    public void addToStatistic(Long user_id, BigDecimal cost){
        UserStatistic byUser = userStatisticRepo.findByUser(userRepo.findById(user_id).orElseThrow());
        byUser.setTotalSpend(byUser.getTotalSpend().add(cost));
        userStatisticRepo.save(byUser);
    }

    public EntityModel<StatisticDTO> getIntervalStatistic(Long user_id, String startDate, String endDate){
        User user = userRepo.findById(user_id).orElseThrow();
        Collection<Purchase> byDayInterval = purchaseRepo.findAllBetweenDates(startDate, endDate, user);
        BigDecimal totalSpendByInterval = byDayInterval.stream()
                .map(Purchase::getPurchaseCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Links links = statisticResourceAssembler.toModel(new UserStatistic(totalSpendByInterval, user)).getLinks();
        return EntityModel.of(new StatisticDTO(totalSpendByInterval), links);
    }

    EntityModel<StatisticDTO> getStatisticEntityModel(User user) {
        UserStatistic entity = userStatisticRepo.findByUser(user);
        Links links = statisticResourceAssembler.toModel(entity).getLinks();
        return EntityModel.of(new StatisticDTO(entity.getTotalSpend()), links);
    }

    EntityModel<StatisticDTO> getStatisticEntityModel(Long user_id) {
        UserStatistic entity = userStatisticRepo.findByUser(userRepo.findById(user_id).orElseThrow());
        Links links = statisticResourceAssembler.toModel(entity).getLinks();
        return EntityModel.of(new StatisticDTO(entity.getTotalSpend()), links);
    }
}
