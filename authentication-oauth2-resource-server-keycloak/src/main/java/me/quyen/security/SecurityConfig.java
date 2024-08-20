package me.quyen.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrfCustomizer ->
                csrfCustomizer.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
        );
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                .requestMatchers(AntPathRequestMatcher.antMatcher("/keycloak/**")).permitAll()

        ).headers(headersCustomizer ->
                headersCustomizer.frameOptions(Customizer.withDefaults()).disable()
        );
        http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                .anyRequest().authenticated()
        ).oauth2ResourceServer(oauth2ResourceServerCustomizer ->
                oauth2ResourceServerCustomizer.jwt(Customizer.withDefaults())
        );
        http.sessionManagement(sessionManagementCustomizer ->
           sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }
}//public class SecurityConfig