package me.quyen.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
@org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthConverter myJwtAuthConverter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
        );
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
        ).headers(headers ->
                        headers.frameOptions(Customizer.withDefaults()).disable()
        );
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer ->authorizeHttpRequestsCustomizer
                .requestMatchers("/keycloak/access-token").permitAll()
                .requestMatchers(HttpMethod.GET,"/restaurant/public/list").permitAll()
                .requestMatchers(HttpMethod.GET,"/restaurant/public/menu/**").permitAll()
                .requestMatchers( "/v3/api-docs/**","/swagger-ui/**","/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
        );
        http.oauth2ResourceServer(oauth2ResourceServerCustomizer ->
                oauth2ResourceServerCustomizer.jwt(jwtCustomizer ->
                        jwtCustomizer.jwtAuthenticationConverter(myJwtAuthConverter)
                )
        );
        http.sessionManagement(sessionManagementCustomizer ->
           sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }
}//public class SecurityConfig