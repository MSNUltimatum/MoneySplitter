package com.dreamteam.moneysplitter.Repositories;

import com.dreamteam.moneysplitter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
