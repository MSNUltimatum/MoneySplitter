package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.RelationshipsController;
import com.dreamteam.moneysplitter.controller.StatisticController;
import com.dreamteam.moneysplitter.domain.dto.Friendship;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RelationshipsResourceAssembler implements RepresentationModelAssembler<Friendship, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(Friendship friendship) {
        return EntityModel.of(friendship.getDestinationUser(),
                linkTo(methodOn(RelationshipsController.class).deleteFriend(friendship.getSourceUser().getId(),
                        friendship.getDestinationUser().getId())).withSelfRel());
    }
}
