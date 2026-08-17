package ru.yandex.practicum.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.yandex.practicum.gateway.roles.Role;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity security) {
        return security.authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/products/**","/api/categories/**","/api/products/1").permitAll()
                        //user
                        .pathMatchers(HttpMethod.GET, "/api/orders/by-email", "/api/orders/*").hasRole(Role.USER.name())
                        .pathMatchers(HttpMethod.POST, "/api/orders/**").hasRole(Role.USER.name())
                        //admin
                        .pathMatchers(HttpMethod.POST, "/api/categories/**", "/api/products/**", "/api/inventory/**").hasRole(Role.ADMIN.name())
                        .pathMatchers(HttpMethod.PATCH, "/api/categories/**", "/api/products/**", "/api/inventory/**").hasRole(Role.ADMIN.name())
                        .pathMatchers(HttpMethod.DELETE, "/api/orders/**", "/api/categories/**", "/api/products/**", "/api/inventory/*  *", "/admin").hasRole(Role.ADMIN.name())
                        .anyExchange().denyAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    MapReactiveUserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails ivan = User.withUsername("ivan")
                .password(passwordEncoder().encode("ivan"))
                .roles(Role.USER.name())
                .build();
        UserDetails anna = User.withUsername("anna")
                .password(passwordEncoder().encode("anna"))
                .roles(Role.USER.name(), Role.ADMIN.name())
                .build();

        return new MapReactiveUserDetailsService(ivan, anna);
    }
}
