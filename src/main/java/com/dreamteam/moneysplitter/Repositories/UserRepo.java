package com.dreamteam.moneysplitter.Repositories;

import com.dreamteam.moneysplitter.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotBlank;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
