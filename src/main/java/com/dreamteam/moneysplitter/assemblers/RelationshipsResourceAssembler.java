package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.RelationshipsController;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.FriendshipDTO;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RelationshipsResourceAssembler implements RepresentationModelAssembler<FriendshipDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(FriendshipDTO friendship) {
        return EntityModel.of(friendship.getDestinationUser(),
                linkTo(methodOn(RelationshipsController.class).deleteFriend(friendship.getDestinationUser().getId())).withSelfRel());
    }
}
