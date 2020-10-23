package com.dreamteam.moneysplitter.domain;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.dreamteam.moneysplitter.domain.UserPermissions.*;

public enum UserRoles {
    USER(Sets.newHashSet(ADD_PURCHASES, GET_STATISTIC, CREATE_EVENTS)), //TODO задать permissions для ролей
    ROOM_CREATOR(Sets.newHashSet(CREATE_EVENTS, ADD_USERS_TO_EVENTS, ADD_PURCHASES, GET_STATISTIC, DELETE_EVENTS)),
    ADMINISTRATOR(null);

    private final Set<UserPermissions> permissions;

    UserRoles(Set<UserPermissions> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermissions> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
