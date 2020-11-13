package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.domain.dto.Friendship;
import com.dreamteam.moneysplitter.domain.dto.FriendshipRequestDTO;
import com.dreamteam.moneysplitter.repositories.FriendshipsRequestRepo;
import com.dreamteam.moneysplitter.repositories.UserRepo;
import com.dreamteam.moneysplitter.assemblers.FriendshipRequestResourceAssembler;
import com.dreamteam.moneysplitter.assemblers.RelationshipsResourceAssembler;
import com.dreamteam.moneysplitter.domain.FriendshipRequest;
import com.dreamteam.moneysplitter.domain.User;
import com.dreamteam.moneysplitter.domain.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RelationshipsService {
    private final UserRepo userRepo;
    private final FriendshipsRequestRepo friendshipsRequestRepo;
    private final FriendshipRequestResourceAssembler resourceAssembler;
    private final RelationshipsResourceAssembler relationshipsResourceAssembler;

    @Autowired
    public RelationshipsService(UserRepo userRepo,
                                FriendshipsRequestRepo friendshipsRequestRepo,
                                FriendshipRequestResourceAssembler resourceAssembler, RelationshipsResourceAssembler relationshipsResourceAssembler) {
        this.userRepo = userRepo;
        this.friendshipsRequestRepo = friendshipsRequestRepo;
        this.resourceAssembler = resourceAssembler;
        this.relationshipsResourceAssembler = relationshipsResourceAssembler;
    }

    public Set<EntityModel<UserDTO>> getUserFriends(Long user_id){
        User user = userRepo.findById(user_id).orElseThrow();
        return user.getFriends().stream()
                .map(e -> relationshipsResourceAssembler.toModel(new Friendship(user, new UserDTO(e.getId(),e.getFirstName(), e.getSecondName(), e.getEmail()))))
                .collect(Collectors.toSet());
    }

    public void sendFriendshipRequest(Long userId, Long friendId){
        User sourceUser= userRepo.findById(userId).orElseThrow();
        User destinationUser = userRepo.findById(friendId).orElseThrow();
        if(!sourceUser.getFriends().contains(destinationUser)) {
            FriendshipRequest friendshipRequest = new FriendshipRequest(sourceUser, destinationUser);
            friendshipsRequestRepo.save(friendshipRequest);
        }
    }

    public void deleteFromFriend(Long userId, Long friendId) {
        User sourceUser= userRepo.findById(userId).orElseThrow();
        User destinationUser = userRepo.findById(friendId).orElseThrow();
        if (sourceUser.getFriends().contains(destinationUser)){
            sourceUser.getFriends().remove(destinationUser);
            userRepo.save(sourceUser);
        } else {
            throw new IllegalStateException();
        }

        if (destinationUser.getFriends().contains(sourceUser)){
            destinationUser.getFriends().remove(sourceUser);
            userRepo.save(destinationUser);
        } else {
            throw new IllegalStateException();
        }
    }

    public Set<EntityModel<FriendshipRequestDTO>> getAllFriendshipRequests(Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        Set<FriendshipRequest> allBySourceUser = friendshipsRequestRepo.findAllByDestinationUser(user);
        return allBySourceUser.stream()
                .map(e -> {
                    Links links = resourceAssembler.toModel(e).getLinks();
                    return EntityModel.of(new FriendshipRequestDTO(new UserDTO(e.getSourceUser().getId(),
                            e.getSourceUser().getFirstName(),
                            e.getSourceUser().getSecondName(),
                            e.getSourceUser().getEmail())), links);
                })
                .collect(Collectors.toSet());
    }

    public EntityModel<FriendshipRequest> getFriendshipRequest(Long requestId) {
        FriendshipRequest friendshipRequest = friendshipsRequestRepo.findById(requestId).orElseThrow();
        return resourceAssembler.toModel(friendshipRequest);
    }

    public void applyRequest(Long requestId, Long userId) {
        FriendshipRequest friendshipRequest = friendshipsRequestRepo.findById(requestId).orElseThrow();
        if(userId.equals(friendshipRequest.getDestinationUser().getId())) {
            friendshipRequest.getDestinationUser().getFriends().add(friendshipRequest.getSourceUser());
            friendshipRequest.getSourceUser().getFriends().add(friendshipRequest.getDestinationUser());
            userRepo.save(friendshipRequest.getDestinationUser());
            userRepo.save(friendshipRequest.getSourceUser());
            friendshipsRequestRepo.delete(friendshipRequest);
        } else {
            throw new IllegalStateException();
        }
    }

    public void rejectRequest(Long requestId, Long userId) {
        FriendshipRequest friendshipRequest = friendshipsRequestRepo.findById(requestId).orElseThrow();
        if(userId.equals(friendshipRequest.getDestinationUser().getId())) {
            friendshipsRequestRepo.delete(friendshipRequest);
        } else {
            throw new IllegalStateException();
        }
    }
}
