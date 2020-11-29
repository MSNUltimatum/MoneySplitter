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
                linkTo(methodOn(EventController.class).getEventById(entity.getEventId())).withSelfRel());
    }
}
