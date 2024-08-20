package me.quyen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/keycloak")
@io.swagger.v3.oas.annotations.security.SecurityRequirement(
        name = "springboot_keycloak"
)
    public class KeyCloakAccessTokenInfoController {
    @GetMapping("/access-token")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationToken getAccessToken(@AuthenticationPrincipal Jwt principal){
        return new JwtAuthenticationToken(principal);
    }

    private final String serverUrl = "http://localhost:8989";
    private final String realm = "springbootrealm";
    private final String clientId = "admin-cli";
    private final String grantType = "password";
    private final String username = "super_user";
    private final String password = "MatKhau@123";
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('SUPER')")
    @GetMapping("/admin-cli/token-manager")
    public org.keycloak.admin.client.token.TokenManager adminCliTokenManager(){
        org.keycloak.admin.client.Keycloak keycloakInstance = org.keycloak.admin.client.KeycloakBuilder.builder()
                .serverUrl(serverUrl).realm(realm)
                .clientId(clientId).grantType(grantType)
                .username(username).password(password)
                .build();
        return keycloakInstance.tokenManager();
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('SUPER')")
    @GetMapping("/admin-cli/users")
    public List<org.keycloak.representations.idm.UserRepresentation>
    keycloakUserRepresentation(){
        org.keycloak.admin.client.Keycloak keycloakInstance = org.keycloak.admin.client.KeycloakBuilder.builder()
                .serverUrl(serverUrl).realm(realm)
                .clientId(clientId).grantType(grantType)
                .username(username).password(password)
                .build();

        return keycloakInstance.realm(realm)
        .users()
        .list();
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('SUPER')")
    @GetMapping("/filters")
    public List<String> filters(
            org.springframework.security.web.FilterChainProxy filterChainProxy){
        return filterChainProxy.getFilterChains().stream()
                .map(SecurityFilterChain::getFilters)
                .flatMap(Collection::stream)
                .map(filter -> filter.getClass().toGenericString())
                .collect(Collectors.toList());
    }
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('SUPER')")
    @GetMapping("/auth-manager")
    public org.springframework.security.authorization.AuthenticatedAuthorizationManager authenticatedAuthorizationManager(
            org.springframework.security.authorization.AuthenticatedAuthorizationManager authenticatedAuthorizationManager
    ){
        return authenticatedAuthorizationManager;
    }

}
