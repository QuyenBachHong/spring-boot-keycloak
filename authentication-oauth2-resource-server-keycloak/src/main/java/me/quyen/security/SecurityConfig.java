package me.quyen.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
        ).headers(headers ->
                        headers.frameOptions(Customizer.withDefaults()).disable()
        ).csrf(csrf ->
                csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
        );
        http.authorizeHttpRequests(authorizedReq ->
                authorizedReq.anyRequest().authenticated()
        ).oauth2ResourceServer(oauth2ResourceServerCustomizer ->
                oauth2ResourceServerCustomizer.jwt(Customizer.withDefaults())
        ).sessionManagement(sessionManagementCustomizer ->
           sessionManagementCustomizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        return http.build();
    }
}//public class SecurityConfig