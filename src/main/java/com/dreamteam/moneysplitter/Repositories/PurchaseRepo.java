package com.dreamteam.moneysplitter.Repositories;

import com.dreamteam.moneysplitter.domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepo extends JpaRepository<Purchase, Long> {
}
