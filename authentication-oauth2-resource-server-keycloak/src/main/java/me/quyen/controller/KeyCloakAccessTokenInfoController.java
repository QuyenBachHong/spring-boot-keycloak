package me.quyen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keycloak")
public class KeyCloakAccessTokenInfoController {
    @GetMapping("/access-token")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationToken getAccessToken(@AuthenticationPrincipal Jwt principal){
        return new JwtAuthenticationToken(principal);
    }
}
