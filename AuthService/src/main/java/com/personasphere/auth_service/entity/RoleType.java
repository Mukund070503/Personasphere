package com.personasphere.auth_service.entity;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {

    USER(Set.of(
            Permissions.PAGE_CREATE,
            Permissions.PAGE_READ_PUBLIC,
            Permissions.PAGE_UPDATE_OWN,
            Permissions.PAGE_CHANGE_VISIBILITY
    )),

    ADMIN(Set.of(
            Permissions.USER_READ,
            Permissions.USER_READ_ALL,
            Permissions.USER_DEACTIVATE,
            Permissions.PAGE_READ_PRIVATE,
            Permissions.PAGE_UPDATE_ANY,
            Permissions.ADMIN_OVERRIDE
    ));

    private final Set<Permissions> permissions;
}
