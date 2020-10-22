package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.UserStatisticRepo;
import com.dreamteam.moneysplitter.assemblers.StatisticResourceAssembler;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class StatisticService {
    @Value("nonserializing.statistic.fields")
    private String nonSerializingStatisticFields;

    private final StatisticResourceAssembler statisticResourceAssembler;
    private final UserStatisticRepo userStatisticRepo;

    @Autowired
    public StatisticService(StatisticResourceAssembler statisticResourceAssembler, UserStatisticRepo userStatisticRepo) {
        this.statisticResourceAssembler = statisticResourceAssembler;
        this.userStatisticRepo = userStatisticRepo;
    }

    public MappingJacksonValue getMonthStatistic(User user){
        EntityModel<UserStatistic> statistic = getUserEntityModel(user);
        return getFilteringJacksonValue(statistic);
    }

    private EntityModel<UserStatistic> getUserEntityModel(User user) {
        UserStatistic entity = userStatisticRepo.findByUser(user);
        return statisticResourceAssembler.toModel(entity);
    }

    private MappingJacksonValue getFilteringJacksonValue(EntityModel<?> userData) {
        MappingJacksonValue wrapper = new MappingJacksonValue(userData);
        wrapper.setFilters(new SimpleFilterProvider()
                .addFilter("statisticFilter",
                        SimpleBeanPropertyFilter.serializeAllExcept(nonSerializingStatisticFields.split(","))));
        return wrapper;
    }

    public Map<String, Object> getIntervalStatistic(User user, LocalDate startDate, LocalDate endDate){
        return null; //TODO доделать
    }
}
