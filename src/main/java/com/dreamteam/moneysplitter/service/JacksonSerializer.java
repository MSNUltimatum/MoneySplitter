package com.dreamteam.moneysplitter.service;

import com.dreamteam.moneysplitter.domain.Purchase;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JacksonSerializer {
    @Value("${nonserializing.user.fields}")
    private String nonSerializingUserFields;

    @Value("${nonserializing.statistic.fields}")
    private String nonSerializingStatisticFields;

    @Value("${nonserializing.purchase.fields}")
    private String nonSerializingPurchaseFields;

    public MappingJacksonValue getFilteringJacksonValueUser(EntityModel<?> userData) {
       return serialize(userData, Map.of("userFilter", nonSerializingUserFields));
    }

    public MappingJacksonValue getFilteringJacksonValueStatistic(EntityModel<?> statisticData) {
        return serialize(statisticData, Map.of("statisticFilter", nonSerializingStatisticFields));
    }

    public MappingJacksonValue getFilteringJacksonValueUserStatistic(Map<String, Object> userAndStatisticData){
        return serialize(userAndStatisticData, Map.of("userFilter", nonSerializingUserFields,
                                                      "statisticFilter", nonSerializingStatisticFields));
    }

    public MappingJacksonValue getFilteringJacksonValuePurchase(CollectionModel<EntityModel<Purchase>> purchases) {
        return serialize(purchases, Map.of("purchaseFilter", nonSerializingPurchaseFields));
    }

    private MappingJacksonValue serialize(Object serializeObject, Map<String, String> filters){
        MappingJacksonValue wrapper = new MappingJacksonValue(serializeObject);
        SimpleFilterProvider simpleFilterProvider = new SimpleFilterProvider();
        filters.forEach((k, v) -> {
            simpleFilterProvider.addFilter(k, SimpleBeanPropertyFilter.serializeAllExcept(v.split(",")));
        });
       wrapper.setFilters(simpleFilterProvider);
       return wrapper;
    }

}
