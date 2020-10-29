package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.EventController;
import com.dreamteam.moneysplitter.controller.PurchaseController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.User;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserResourceAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserProfileController.class).myProfile(entity.getId())).withSelfRel(),
                linkTo(methodOn(EventController.class).getMyEvents(entity.getId())).withRel("events"),
                linkTo(methodOn(PurchaseController.class).addOwnPurchase(entity.getId(), null)).withRel("addPurchase"),
                linkTo(methodOn(PurchaseController.class).getAllUserPurchases(entity.getId())).withRel("addPurchase")
                );
    }
}
