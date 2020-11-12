package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.PurchaseRepo;
import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.assemblers.PurchaseResourceAssembler;
import com.dreamteam.moneysplitter.controller.PurchaseController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PurchaseService {

    private final PurchaseRepo purchaseRepo;
    private final UserRepo userRepo;
    private final PurchaseResourceAssembler resourceAssembler;
    private final StatisticService statisticService;

    @Autowired
    public PurchaseService(PurchaseRepo purchaseRepo,
                           UserRepo userRepo,
                           PurchaseResourceAssembler resourceAssembler,
                           StatisticService statisticService) {
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
        this.resourceAssembler = resourceAssembler;
        this.statisticService = statisticService;
    }

    public EntityModel<PurchaseDTO> addOwnUserPurchase(Long user_id, Purchase purchase){
        purchase.setUser(userRepo.findById(user_id).orElseThrow());
        purchase.setDate(LocalDate.now().toString());
        statisticService.addToStatistic(user_id, purchase.getPurchaseCost());
        Purchase save = purchaseRepo.save(purchase);
        Links links = resourceAssembler.toModel(save).getLinks();
        return EntityModel.of(new PurchaseDTO(purchase.getPurchaseName(), purchase.getPurchaseCost(), purchase.getDate()), links);
    }

    public List<EntityModel<PurchaseDTO>> getAllUserPurchases(Long user_id) {
        List<Purchase> purchases = purchaseRepo.findAllByUser(userRepo.findById(user_id).orElseThrow());
        return purchases.stream().map(e -> {
            Links links = resourceAssembler.toModel(e).getLinks();
            return EntityModel.of(new PurchaseDTO(e.getPurchaseName(), e.getPurchaseCost(), e.getDate()), links);
        }).collect(Collectors.toList());
    }

    public EntityModel<PurchaseDTO> getOnePurchase(Long purchase_id) {
        Purchase entity = purchaseRepo.findById(purchase_id).orElseThrow();
        Links links = resourceAssembler.toModel(entity).getLinks();
        return EntityModel.of(new PurchaseDTO(entity.getPurchaseName(), entity.getPurchaseCost(), entity.getDate()), links);
    }

    private CollectionModel<EntityModel<Purchase>> getCollectionModel(List<EntityModel<Purchase>> purchase, Long user_id){
        return CollectionModel.of(purchase,
                linkTo(methodOn(UserProfileController.class).myProfile(user_id)).withRel("user"),
                linkTo(methodOn(PurchaseController.class).getAllUserPurchases(user_id)).withSelfRel());
    }

}
