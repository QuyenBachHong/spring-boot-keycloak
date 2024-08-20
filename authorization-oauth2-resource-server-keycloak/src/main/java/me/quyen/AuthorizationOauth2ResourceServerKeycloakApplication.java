package me.quyen;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@io.swagger.v3.oas.annotations.security.SecurityScheme(
		name = "springboot_keycloak"
		,openIdConnectUrl = "http://localhost:8989/realms/springbootrealm/.well-known/openid-configuration"
		,scheme = "bearer"
		,type = io.swagger.v3.oas.annotations.enums.SecuritySchemeType.OPENIDCONNECT
		,in = io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
)
public class AuthorizationOauth2ResourceServerKeycloakApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthorizationOauth2ResourceServerKeycloakApplication.class, args);
	}

}
