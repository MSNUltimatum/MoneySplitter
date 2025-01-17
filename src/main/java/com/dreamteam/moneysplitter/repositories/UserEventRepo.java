package com.dreamteam.moneysplitter.repositories;

import com.dreamteam.moneysplitter.domain.Event;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.UserEvent;
import com.dreamteam.moneysplitter.domain.UserEventKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserEventRepo extends JpaRepository<UserEvent, UserEventKey> {
    List<UserEvent> findAllByUser(User user);

    List<UserEvent> findAllByEvent(Event event);
}
