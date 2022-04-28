package com.lenovo.training.edge.service.impl;

import com.lenovo.training.edge.service.AuthService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger("AuthServiceImpl");

    @Override
    public AccessToken getToken() {
        LOGGER.info("Getting token");
        return getKeycloakPrincipal().getKeycloakSecurityContext().getToken();
    }

    @Override
    public KeycloakSecurityContext getSecurityContext() {
        LOGGER.info("Getting KeycloakSecurityContext");
        return getKeycloakPrincipal().getKeycloakSecurityContext();
    }

    private KeycloakPrincipal<KeycloakSecurityContext> getKeycloakPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal<KeycloakSecurityContext> principal =
            (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
        LOGGER.info("Getting KeycloakPrincipal");
        return principal;
    }
}
