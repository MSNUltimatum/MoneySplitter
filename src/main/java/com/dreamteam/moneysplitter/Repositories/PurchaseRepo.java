package com.dreamteam.moneysplitter.Repositories;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByUser(User user);

    @Query("SELECT p FROM Purchase p WHERE p.date >= ?1 AND p.date <= ?2 AND p.user = ?3")
    Collection<Purchase> findAllBetweenDates(String startDate, String endDate, User user);
}
