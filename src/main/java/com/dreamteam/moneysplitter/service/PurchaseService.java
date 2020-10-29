package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.PurchaseRepo;
import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.assemblers.PurchaseResourceAssembler;
import com.dreamteam.moneysplitter.controller.PurchaseController;
import com.dreamteam.moneysplitter.controller.UserProfileController;
import com.dreamteam.moneysplitter.domain.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
    private final JacksonSerializer jacksonSerializer;
    private final PurchaseResourceAssembler resourceAssembler;
    private final StatisticService statisticService;

    @Autowired
    public PurchaseService(PurchaseRepo purchaseRepo,
                           UserRepo userRepo,
                           JacksonSerializer jacksonSerializer,
                           PurchaseResourceAssembler resourceAssembler,
                           StatisticService statisticService) {
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
        this.jacksonSerializer = jacksonSerializer;
        this.resourceAssembler = resourceAssembler;
        this.statisticService = statisticService;
    }

    public MappingJacksonValue addOwnUserPurchase(Long user_id, Purchase purchase){
        purchase.setUser(userRepo.findById(user_id).orElseThrow());
        purchase.setDate(LocalDate.now().toString());
        statisticService.addToStatistic(user_id, purchase.getPurchaseCost());
        Purchase save = purchaseRepo.save(purchase);
        EntityModel<Purchase> purchaseEntityModel = resourceAssembler.toModel(save);
        return jacksonSerializer.getFilteringJacksonValuePurchase(getCollectionModel(List.of(purchaseEntityModel), user_id));
    }

    public MappingJacksonValue getAllUserPurchases(Long user_id) {
        List<Purchase> purchases = purchaseRepo.findAllByUser(userRepo.findById(user_id).orElseThrow());
        List<EntityModel<Purchase>> purchasesResource = purchases.stream().map(resourceAssembler::toModel).collect(Collectors.toList());
        return jacksonSerializer.getFilteringJacksonValuePurchase(getCollectionModel(purchasesResource, user_id));
    }

    public MappingJacksonValue getOnePurchase(Long purchase_id) {
        Purchase entity = purchaseRepo.findById(purchase_id).orElseThrow();
        EntityModel<Purchase> purchase = resourceAssembler.toModel(entity);
        return jacksonSerializer.getFilteringJacksonValuePurchase(getCollectionModel(List.of(purchase), entity.getUser().getId()));
    }

    private CollectionModel<EntityModel<Purchase>> getCollectionModel(List<EntityModel<Purchase>> purchase, Long user_id){
        return CollectionModel.of(purchase,
                linkTo(methodOn(UserProfileController.class).myProfile(user_id)).withRel("user"),
                linkTo(methodOn(PurchaseController.class).getAllUserPurchases(user_id)).withSelfRel());
    }

}
