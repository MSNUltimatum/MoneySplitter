package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/{id}/getMonthStatistic")
    public ResponseEntity<Object> getMonthStatistic(@PathVariable("id") User user){
        MappingJacksonValue statistic = statisticService.getMonthStatistic(user);
        return ResponseEntity.ok().body(statistic);
    }

    @GetMapping("/{id}/{startDate}/{endDate}")
    public ResponseEntity<Object> getPeriodStatistic(@PathVariable("id") User user,
                                                     @PathVariable Optional<LocalDate> startDate,
                                                     @PathVariable Optional<LocalDate> endDate){
        if(startDate.isPresent() && endDate.isPresent()){
            return ResponseEntity.ok().body(statisticService.getIntervalStatistic(user,
                                                                                  startDate.get(),
                                                                                  endDate.get()));
        } else {
            return getMonthStatistic(user);
        }
    }
}