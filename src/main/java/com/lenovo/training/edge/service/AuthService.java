package com.lenovo.training.edge.service;

import org.keycloak.representations.AccessToken;

public interface AuthService {

    AccessToken getToken();
}