package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.StatisticController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.UserStatistic;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StatisticResourceAssembler implements RepresentationModelAssembler<UserStatistic, EntityModel<UserStatistic>> {
    @Override
    public EntityModel<UserStatistic> toModel(UserStatistic entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StatisticController.class).getMonthStatistic(entity.getUser().getId())).withSelfRel(),
                linkTo(methodOn(UserProfileController.class).myProfile(entity.getUser().getId())).withRel("userProfile"),
                linkTo(methodOn(StatisticController.class).getPeriodStatistic(entity.getUser().getId(),
                                                                              "",
                                                                              "")).withRel("intervalStatistic"));
    }
}
