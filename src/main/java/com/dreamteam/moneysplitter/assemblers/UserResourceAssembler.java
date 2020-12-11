package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.*;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements RepresentationModelAssembler<UserDTO, EntityModel<UserDTO>> {
    @Override
    public EntityModel<UserDTO> toModel(UserDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserProfileController.class).myProfile()).withSelfRel(),
                linkTo(methodOn(EventController.class).getMyEvents()).withRel("events"),
                linkTo(methodOn(PurchaseController.class).addOwnPurchase(null)).withRel("addPurchase"),
                linkTo(methodOn(PurchaseController.class).getAllUserPurchases()).withRel("getAllPurchases"),
                linkTo(methodOn(RelationshipsController.class).getMyFriends()).withRel("getAllFriends"),
                linkTo(methodOn(FriendshipRequestsController.class).getAllUserFriendshipRequests()).withRel("allFriendshipsRequests"),
                linkTo(methodOn(EventController.class).getMyEvents()).withRel("allMyEvents"),
                linkTo(methodOn(EventController.class).createEvent(null)).withRel("/createEvent")
        );
    }
}
