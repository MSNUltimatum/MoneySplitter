package com.dreamteam.moneysplitter.domain;

public enum  UserPermissions {
    ADD_PURCHASES("add_purchase"),
    GET_STATISTIC("get_statistic"),
    CREATE_EVENTS("create_events"),
    DELETE_EVENTS("delete_events"),
    ADD_USERS_TO_EVENTS("add_user_to_events")
    ;

    UserPermissions(String permission) {
        this.permission = permission;
    }

    private String permission;

    public String getPermission() {
        return permission;
    }
}
