package com.diplom.marketplace.entity.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Role
 *
 * @author Sainjargal Ishdorj
 **/

public enum Role implements GrantedAuthority {

    ROLE_USER, ROLE_ADMIN;

    public String getAuthority() {
        return name();
    }

}
