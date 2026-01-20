package com.kiselev.sergey.security.user.model;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum Role {
    ADMIN(Set.of(Permission.READ, Permission.WRITE)),
    USER(Set.of(Permission.READ));

    private final Set<Permission> permission;

    Role(Set<Permission> permission) {
        this.permission = permission;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermission().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
