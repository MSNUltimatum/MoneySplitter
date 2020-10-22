package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.Repositories.UserRepo;
import com.dreamteam.moneysplitter.assemblers.UserResourceAssembler;
import com.dreamteam.moneysplitter.domain.User;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Value("nonserializing.user.fields")
    private String nonSerializingUserFields;

    private final UserRepo userRepo;
    private final UserResourceAssembler userResourceAssembler;

    @Autowired
    public UserService(UserRepo userRepo,
                       UserResourceAssembler userResourceAssembler) {
        this.userRepo = userRepo;
        this.userResourceAssembler = userResourceAssembler;
    }

    public MappingJacksonValue getUser(Long user_id){
        EntityModel<User> user = getUserEntityModel(user_id);
        return getFilteringJacksonValue(user);
    }

    private EntityModel<User> getUserEntityModel(Long user_id) {
        User entity = userRepo.findById(user_id).orElseThrow();
        return userResourceAssembler.toModel(entity);
    }

    private MappingJacksonValue getFilteringJacksonValue(EntityModel<?> userData) {
        MappingJacksonValue wrapper = new MappingJacksonValue(userData);
        wrapper.setFilters(new SimpleFilterProvider()
                .addFilter("userFilter",
                        SimpleBeanPropertyFilter.serializeAllExcept(nonSerializingUserFields.split(","))));
        return wrapper;
    }
}
