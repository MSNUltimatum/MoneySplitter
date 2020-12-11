package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.controller.EventController;
import com.dreamteam.moneysplitter.domain.dto.EventDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EventResourceAssembler implements RepresentationModelAssembler<EventDTO, EntityModel<EventDTO>> {

    @Override
    public EntityModel<EventDTO> toModel(EventDTO entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(EventController.class).getEventById(entity.getEventId())).withSelfRel(),
                linkTo(methodOn(EventController.class).getMyEvents()).withRel("allMyEvents"),
                linkTo(methodOn(EventController.class).addPurchaseToEvent(entity.getEventId(), null)).withRel("addPurchase"),
                linkTo(methodOn(EventController.class).closeEvent(entity.getEventId())).withRel("closeEvent"),
                linkTo(methodOn(EventController.class).split(entity.getEventId())).withRel("split"),
                linkTo(methodOn(EventController.class).addUserToEvent(null, entity.getEventId())).withRel("addUser"));
    }
}
