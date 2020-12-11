package com.dreamteam.moneysplitter.repositories;

import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatisticRepo extends JpaRepository<UserStatistic, Long> {
    UserStatistic findByUser(User user);
}
