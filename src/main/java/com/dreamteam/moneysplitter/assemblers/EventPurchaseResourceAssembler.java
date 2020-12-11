package com.dreamteam.moneysplitter.assemblers;

import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EventPurchaseResourceAssembler implements RepresentationModelAssembler<PurchaseDTO, EntityModel<PurchaseDTO>> {
    @Override
    public EntityModel<PurchaseDTO> toModel(PurchaseDTO entity) {
        return EntityModel.of(entity);
    }
}
