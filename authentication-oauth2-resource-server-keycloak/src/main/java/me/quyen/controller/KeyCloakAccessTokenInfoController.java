package me.quyen.controller;

import me.quyen.security.KeycloakSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/keycloak")
public class KeyCloakAccessTokenInfoController {
    @Autowired
    private KeycloakSecurityUtil keycloakSecurityUtil;
    @GetMapping("/access-token")
    @ResponseStatus(HttpStatus.OK)
    public JwtAuthenticationToken getAccessToken(@AuthenticationPrincipal Jwt principal){
        return new JwtAuthenticationToken(principal);
    }

    @GetMapping("/admin-cli/token-manager")
    public org.keycloak.admin.client.token.TokenManager keycloakTokenManager(){
        return keycloakSecurityUtil.getKeycloakInstance().tokenManager();
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(String refreshToken) {
        //http://localhost:8989/realms/springbootrealm/protocol/openid-connect/token
        String realm = "springbootrealm";
        String url = "http://localhost:8989/realms/" + realm + "/protocol/openid-connect/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String clientId = "springbootclientid";
        String clientSecret = "R1cF3HJYRlOeyj6bBs8JTrCsft2MhJxX";
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", refreshToken);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        ResponseEntity<Object> response = restTemplate.exchange(url,
                HttpMethod.POST,
                entity,
                Object.class);
        return response;
    }
}
