package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<MappingJacksonValue> addOwnPurchase(@NotNull @PathVariable("id")Long user_id,
                                                              @RequestBody(required = false) Purchase purchase){
        if(purchase != null) {
            MappingJacksonValue mappingJacksonValue = purchaseService.addOwnUserPurchase(user_id, purchase);
            return ResponseEntity.ok(mappingJacksonValue);
        }
        return (ResponseEntity<MappingJacksonValue>) ResponseEntity.notFound();
    }

    @GetMapping("{id}/getAllUserPurchases")
    public ResponseEntity<MappingJacksonValue> getAllUserPurchases(@NotNull @PathVariable("id")Long user_id){
        MappingJacksonValue allUserPurchases = purchaseService.getAllUserPurchases(user_id);
        return ResponseEntity.ok(allUserPurchases);
    }

    @GetMapping("/getOnePurchase/{purchase_id}")
    public ResponseEntity<MappingJacksonValue> getOnePurchase(@PathVariable("purchase_id")Long purchase_id){
        return ResponseEntity.ok(purchaseService.getOnePurchase(purchase_id));
    }
}
