package com.personasphere.auth_service.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permissions {

    USER_READ("user:read"),
    USER_READ_ALL("user:read:all"),
    USER_DEACTIVATE("user:deactivate"),

    PAGE_CREATE("page:create"),
    PAGE_READ_PUBLIC("page:read:public"),
    PAGE_READ_PRIVATE("page:read:private"),
    PAGE_UPDATE_OWN("page:update:own"),
    PAGE_UPDATE_ANY("page:update:any"),
    PAGE_CHANGE_VISIBILITY("page:change:visibility"),

    ADMIN_OVERRIDE("admin:override");

	private final String permission;
}
