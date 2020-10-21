package com.dreamteam.moneysplitter.Repositories;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
    Collection<Purchase> findAllByUser(User user);
}
