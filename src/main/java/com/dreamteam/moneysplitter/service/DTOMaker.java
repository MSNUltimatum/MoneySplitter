package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.domain.Event;
import com.dreamteam.moneysplitter.domain.Purchase;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.EventDTO;
import com.dreamteam.moneysplitter.domain.dto.PurchaseDTO;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;

public class DTOMaker {
    public static UserDTO getUserDTO(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getSecondName(), user.getEmail());
    }

    public static EventDTO getEventDTO(Event event) {
        return new EventDTO(event.getId(), event.getDateOpen(), event.getDateClosed(), getUserDTO(event.getOwner()), event.getName());
    }

    public static PurchaseDTO getPurchaseDTO(Purchase purchase) {
        return new PurchaseDTO(getUserDTO(purchase.getUser()), purchase.getPurchaseName(), purchase.getPurchaseCost(), purchase.getDate());
    }
}
