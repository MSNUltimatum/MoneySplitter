package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.repositories.UserStatisticRepo;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Configuration
@EnableScheduling
public class ScheduleService {
    private final UserStatisticRepo userStatisticRepo;

    @Autowired
    public ScheduleService(UserStatisticRepo userStatisticRepo) {
        this.userStatisticRepo = userStatisticRepo;
    }

    @Scheduled(cron = "0 0 1 * * *")
    private void RebootStatistic() {
        List<UserStatistic> all = userStatisticRepo.findAll();
        all.forEach(e -> {
            e.setTotalSpend(BigDecimal.ZERO);
            e.setUpdatingDate(LocalDate.now().toString());
        });
        all.forEach(userStatisticRepo::save);
    }
}
