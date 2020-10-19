package com.dreamteam.moneysplitter.Repositories;

import com.dreamteam.moneysplitter.domain.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatisticRepo extends JpaRepository<UserStatistic, Long> {
}
