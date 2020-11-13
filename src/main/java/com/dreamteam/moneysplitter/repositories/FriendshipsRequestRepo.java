package com.dreamteam.moneysplitter.repositories;

import com.dreamteam.moneysplitter.domain.FriendshipRequest;
import com.dreamteam.moneysplitter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FriendshipsRequestRepo extends JpaRepository<FriendshipRequest, Long> {
    Set<FriendshipRequest> findAllBySourceUser(User sourceUser);
    Set<FriendshipRequest> findAllByDestinationUser(User user);
}
