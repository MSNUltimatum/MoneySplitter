package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.repositories.PurchaseRepo;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import com.dreamteam.moneysplitter.assemblers.PrivatePurchaseResourceAssembler;
import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class PurchaseService {

    private final PurchaseRepo purchaseRepo;
    private final UserRepo userRepo;
    private final PrivatePurchaseResourceAssembler resourceAssembler;
    private final StatisticService statisticService;

    @Autowired
    public PurchaseService(PurchaseRepo purchaseRepo,
                           UserRepo userRepo,
                           PrivatePurchaseResourceAssembler resourceAssembler,
                           StatisticService statisticService) {
        this.purchaseRepo = purchaseRepo;
        this.userRepo = userRepo;
        this.resourceAssembler = resourceAssembler;
        this.statisticService = statisticService;
    }

    public EntityModel<PurchaseDTO> addOwnUserPurchase(String principal, Purchase purchase) {
        purchase.setUser(userRepo.findByEmail(principal));
        purchase.setDate(LocalDate.now().toString());
        statisticService.addToStatistic(principal, purchase.getPurchaseCost());
        Purchase save = purchaseRepo.save(purchase);
        Links links = resourceAssembler.toModel(save).getLinks();
        return EntityModel.of(DTOMaker.getPurchaseDTO(purchase), links);
    }

    public List<EntityModel<PurchaseDTO>> getAllUserPurchases(String principal) {
        List<Purchase> purchases = purchaseRepo.findAllByUser(userRepo.findByEmail(principal));
        return purchases.stream().map(e -> {
            Links links = resourceAssembler.toModel(e).getLinks();
            return EntityModel.of(DTOMaker.getPurchaseDTO(e), links);
        }).collect(Collectors.toList());
    }

    public EntityModel<PurchaseDTO> getOnePurchase(Long purchase_id) {
        Purchase entity = purchaseRepo.findById(purchase_id).orElseThrow();
        Links links = resourceAssembler.toModel(entity).getLinks();
        return EntityModel.of(DTOMaker.getPurchaseDTO(entity), links);
    }

//    private CollectionModel<EntityModel<Purchase>> getCollectionModel(List<EntityModel<Purchase>> purchase, Long user_id){
//        return CollectionModel.of(purchase,
//                linkTo(methodOn(UserProfileController.class).myProfile(user_id)).withRel("user"),
//                linkTo(methodOn(PurchaseController.class).getAllUserPurchases(user_id)).withSelfRel());
//    }

}
