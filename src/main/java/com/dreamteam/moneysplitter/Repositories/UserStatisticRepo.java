package com.dreamteam.moneysplitter.Repositories;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;

public interface UserStatisticRepo extends JpaRepository<UserStatistic, Long> {
    UserStatistic findByUser(User user);

    @Query(value = "SELECT p FROM Purchase p WHERE p.date >= ?1 AND  p.date <= ?2")
    Collection<Purchase> findByDayInterval(LocalDate startDate, LocalDate endDate);
}
