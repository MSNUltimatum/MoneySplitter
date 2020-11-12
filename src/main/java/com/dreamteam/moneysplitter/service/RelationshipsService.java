package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.assemblers.RelationshipsResourceAssembler;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RelationshipsService {
    private final UserRepo userRepo;
    private final RelationshipsResourceAssembler relationshipsResourceAssembler;

    @Autowired
    public RelationshipsService(UserRepo userRepo, RelationshipsResourceAssembler relationshipsResourceAssembler) {
        this.userRepo = userRepo;
        this.relationshipsResourceAssembler = relationshipsResourceAssembler;
    }

    public Set<EntityModel<UserDTO>> getUserFriends(Long user_id){
        User user = userRepo.findById(user_id).orElseThrow();
        return user.getFriends().stream()
                .map(e -> relationshipsResourceAssembler.toModel(new UserDTO(e.getId(),e.getFirstName(), e.getSecondName(), e.getEmail())))
                .collect(Collectors.toSet());
    }
}
