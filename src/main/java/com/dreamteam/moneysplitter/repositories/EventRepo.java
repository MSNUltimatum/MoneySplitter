package com.dreamteam.moneysplitter.repositories;

import com.dreamteam.moneysplitter.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Long> {
}
