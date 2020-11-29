package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.FriendshipRequestsController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.FriendshipRequest;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FriendshipRequestResourceAssembler implements RepresentationModelAssembler<FriendshipRequest, EntityModel<FriendshipRequest>> {
    private final UserRepo userRepo;

    @Autowired
    public FriendshipRequestResourceAssembler(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public EntityModel<FriendshipRequest> toModel(FriendshipRequest entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserProfileController.class).myProfile()).withRel("profile"),
                linkTo(methodOn(FriendshipRequestsController.class).getAllUserFriendshipRequests()).withRel("allUserRequests"),
                linkTo(methodOn(FriendshipRequestsController.class).getOneFriendshipRequest(entity.getRequestId())).withSelfRel(),
                linkTo(methodOn(FriendshipRequestsController.class).applyRequest(entity.getRequestId())).withRel("apply"),
                linkTo(methodOn(FriendshipRequestsController.class).rejectRequest(entity.getRequestId())).withRel("reject")
        );
    }
}
