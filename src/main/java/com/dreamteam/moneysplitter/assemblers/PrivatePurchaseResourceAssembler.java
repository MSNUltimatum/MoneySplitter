package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.PurchaseController;
import com.dreamteam.moneysplitter.controller.StatisticController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PrivatePurchaseResourceAssembler implements RepresentationModelAssembler<Purchase, EntityModel<Purchase>> {

    @Override
    public EntityModel<Purchase> toModel(Purchase entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PurchaseController.class).getOnePurchase(entity.getId())).withSelfRel(),
                linkTo(methodOn(PurchaseController.class).getAllUserPurchases()).withRel("allUserPurchases"),
                linkTo(methodOn(UserProfileController.class).myProfile()).withRel("user"));
    }
}
