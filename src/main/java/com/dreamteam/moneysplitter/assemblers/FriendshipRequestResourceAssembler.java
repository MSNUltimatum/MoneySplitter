package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.FriendshipRequestsController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.FriendshipRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FriendshipRequestResourceAssembler implements RepresentationModelAssembler<FriendshipRequest, EntityModel<FriendshipRequest>> {

    @Override
    public EntityModel<FriendshipRequest> toModel(FriendshipRequest entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(UserProfileController.class).myProfile(entity.getDestinationUser().getId())).withRel("profile"),
                linkTo(methodOn(FriendshipRequestsController.class).getAllUserFriendshipRequests(entity.getSourceUser().getId())).withRel("allUserRequests"),
                linkTo(methodOn(FriendshipRequestsController.class).getOneFriendshipRequest(entity.getRequestId())).withSelfRel(),
                linkTo(methodOn(FriendshipRequestsController.class).applyRequest(entity.getDestinationUser().getId(), entity.getRequestId())).withRel("apply"),
                linkTo(methodOn(FriendshipRequestsController.class).rejectRequest(entity.getDestinationUser().getId(), entity.getRequestId())).withRel("reject")
        );
    }
}
