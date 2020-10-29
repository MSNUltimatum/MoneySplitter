package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.PurchaseController;
import com.dreamteam.moneysplitter.controller.StatisticController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.Purchase;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PurchaseResourceAssembler implements RepresentationModelAssembler<Purchase, EntityModel<Purchase>> {

    @Override
    public EntityModel<Purchase> toModel(Purchase entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(PurchaseController.class).getOnePurchase(entity.getId())).withSelfRel(),
                linkTo(methodOn(PurchaseController.class).getAllUserPurchases(entity.getUser().getId())).withRel("allUserPurchases"),
                linkTo(methodOn(UserProfileController.class).myProfile(entity.getUser().getId())).withRel("user"));
    }
}
