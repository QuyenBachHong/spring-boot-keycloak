package me.quyen.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * {
 *   ......
 *   "allowed-origins": [
 *     "*"
 *   ],
 *   "realm_access": {
 *     "roles": [
 *       "default-roles-springbootrealm",
 *       "AUTHENTICATED",
 *       "offline_access",
 *       .......
 *       "uma_authorization"
 *     ]
 *   },
 *   "resource_access": {
 *     "account": {
 *       "roles": [
 *         "manage-account",
 *         "manage-account-links",
 *         "view-profile"
 *       ]
 *     }
 *   },
 *   "scope": "profile email",
 *   "email_verified": true,
 *   "address": {
 *     .....
 *   },
 *   ........
 *   "preferred_username": "manager_user",
 *   .........
 * }
 * JWT issued by the Keycloak server upon
 * successful login contains `realm_access` and `resource_access` claims
 * in which roles are incorporated. This can be verified by decoding the token.
 *
 * To create custom claims on our resource server, we need a converter
 * that will extract roles from given JWT and convert them to a collection
 * of granted authorities. We previously opted for the realm level role,
 * so we will extract roles from `realm_access` claim.
 */
@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {


    /**
     * Convert the source object of type {@code S} to target type {@code T}.
     *
     * @param jwt the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return the converted object, which must be an instance of {@code T} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter
                =
                new org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter();
        Set<GrantedAuthority> grantedAuthorities = Stream.concat(
                        jwtGrantedAuthoritiesConverter.convert(jwt).stream(), extractAuthorities(jwt).stream()
                )
                .collect(Collectors.toCollection(LinkedHashSet::new));
        //=================SUPER_USER===========================================================
        String preferredUsername = (jwt.getClaim("preferred_username") != null)?
                jwt.getClaim("preferred_username").toString() : null;
        String azp = (jwt.getClaim("azp") != null) ? jwt.getClaim("azp").toString() : null;
        boolean preferredUsernameExisted = (preferredUsername != null) && (superUser.contains(preferredUsername.toUpperCase()));
        boolean azpExisted = (azp != null) && (superUser.contains(azp.toUpperCase()));
        if((preferredUsernameExisted) || (azpExisted)) {
            superUserRoles.forEach($ -> grantedAuthorities.add(new SimpleGrantedAuthority($)));
        }
        //============================================================================
        return new JwtAuthenticationToken(jwt,grantedAuthorities);
    }

    /**
     *   "realm_access": {
     *     "roles": [
     *       "default-roles-springbootrealm",
     *       "AUTHENTICATED",
     *       "offline_access",
     *       .......
     *       "uma_authorization"
     *     ]
     *   },
     */
    private final String jwtRealmAccess = "realm_access";
    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt){
        if ((jwt.getClaim(jwtRealmAccess)) != null) //realm_access.roles
        {
            Map<String,Object> realmAccess = jwt.getClaim(jwtRealmAccess);
            ObjectMapper mapper = new ObjectMapper();
            LinkedHashSet<String> roles = new LinkedHashSet<>(
                    mapper.convertValue(realmAccess.get("roles"),new TypeReference<ArrayList<String>>() {})
            );
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return new LinkedHashSet<>();
    }
    private List<String> superUser = new ArrayList<>(
            Set.of("SUPER","SUPER_USER","SUPER-USER","SUDO","SUDOER","ADMIN-CLI")
    );
    private List<String> superUserRoles = new ArrayList<>(
            Set.of(
                    "ROLE_AUTHENTICATED","ROLE_OWNER","ROLE_MANAGER","ROLE_ADMIN",
                    "ROLE_REST_UPDATE","ROLE_REST_READ", "ROLE_REST_DELETE",
                    "ROLE_REST_CREATE","ROLE_REST_CRUD","ROLE_SUPER"
            )
    );
}