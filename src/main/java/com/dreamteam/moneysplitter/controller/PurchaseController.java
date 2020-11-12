package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import com.dreamteam.moneysplitter.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/purchases/")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("{id}/addOwnPurchase")
    public ResponseEntity<Object> addOwnPurchase(@NotNull @PathVariable("id")Long user_id,
                                                              @RequestBody(required = false) Purchase purchase){
        if(purchase != null) {
            EntityModel<PurchaseDTO> mappingJacksonValue = purchaseService.addOwnUserPurchase(user_id, purchase);
            return ResponseEntity.ok(mappingJacksonValue);
        }
        return (ResponseEntity<Object>) ResponseEntity.notFound();
    }

    @GetMapping("{id}/getAllUserPurchases")
    public ResponseEntity<Object> getAllUserPurchases(@NotNull @PathVariable("id")Long user_id){
        List<EntityModel<PurchaseDTO>> allUserPurchases = purchaseService.getAllUserPurchases(user_id);
        return ResponseEntity.ok(allUserPurchases);
    }

    @GetMapping("/getOnePurchase/{purchase_id}")
    public ResponseEntity<Object> getOnePurchase(@PathVariable("purchase_id")Long purchase_id){
        return ResponseEntity.ok(purchaseService.getOnePurchase(purchase_id));
    }
}
