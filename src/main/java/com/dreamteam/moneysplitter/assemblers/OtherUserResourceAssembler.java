package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OtherUserResourceAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(UserDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserProfileController.class).otherUser(entity.getId())).withRel("getProfile"));
    }
}
