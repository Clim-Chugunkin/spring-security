package com.kiselev.sergey.security.user.model;

import lombok.Getter;

@Getter
public enum Permission {
    READ("read"),
    WRITE("write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

}
