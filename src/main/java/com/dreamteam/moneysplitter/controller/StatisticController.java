package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.StatisticDTO;
import com.dreamteam.moneysplitter.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/getMonthStatistic")
    public ResponseEntity<Object> getMonthStatistic(){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        EntityModel<StatisticDTO> statistic = statisticService.getMonthStatistic(principal);
        return ResponseEntity.ok().body(statistic);
    }

    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<Object> getPeriodStatistic(@PathVariable("startDate") String startDate,
                                                     @PathVariable("endDate") String endDate){
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!startDate.isBlank() && !endDate.isBlank()){
            return ResponseEntity.ok().body(statisticService.getIntervalStatistic(principal,
                                                                                  startDate,
                                                                                  endDate));
        } else {
            return getMonthStatistic();
        }
    }
}
