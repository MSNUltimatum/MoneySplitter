package com.dreamteam.moneysplitter.controller;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import com.dreamteam.moneysplitter.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping("/addOwnPurchase")
    public ResponseEntity<Object> addOwnPurchase(@RequestBody(required = false) Purchase purchase) {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (purchase != null && !principal.isBlank()) {
            EntityModel<PurchaseDTO> mappingJacksonValue = purchaseService.addOwnUserPurchase(principal, purchase);
            return ResponseEntity.ok(mappingJacksonValue);
        }
        return (ResponseEntity<Object>) ResponseEntity.notFound();
    }

    @GetMapping("/getAllUserPurchases")
    public ResponseEntity<Object> getAllUserPurchases() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntityModel<PurchaseDTO>> allUserPurchases = purchaseService.getAllUserPurchases(principal);
        return ResponseEntity.ok(allUserPurchases);
    }

    @GetMapping("/getOnePurchase/{purchase_id}")
    public ResponseEntity<Object> getOnePurchase(@PathVariable("purchase_id") Long purchase_id) {
        return ResponseEntity.ok(purchaseService.getOnePurchase(purchase_id));
    }
}
