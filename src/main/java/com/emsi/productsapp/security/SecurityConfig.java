package com.emsi.productsapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ─────────────────────────────────────────────────────────
    // USERS en mémoire (3 rôles : USER, ADMIN, MANAGER)
    // ─────────────────────────────────────────────────────────
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var manager = new InMemoryUserDetailsManager();

        manager.createUser(User.withUsername("user")
                .password(encoder.encode("1234"))
                .roles("USER")
                .build());

        manager.createUser(User.withUsername("admin")
                .password(encoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build());

        manager.createUser(User.withUsername("manager")
                .password(encoder.encode("manager"))
                .roles("USER", "ADMIN", "MANAGER")
                .build());

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ─────────────────────────────────────────────────────────
    // RÈGLES DE SÉCURITÉ
    // ─────────────────────────────────────────────────────────
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Ressources statiques et H2 console accessibles à tous
                .requestMatchers("/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                // Lecture (liste + recherche) : USER suffit
                .requestMatchers("/", "/products").hasRole("USER")
                // Ajout / Modification : ADMIN requis
                .requestMatchers("/products/new", "/products/save",
                                 "/products/edit/**", "/products/update/**").hasRole("ADMIN")
                // Suppression : ADMIN requis
                .requestMatchers("/products/delete/**").hasRole("ADMIN")
                // Tout le reste : authentifié
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/products", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // Pour H2 console (désactiver frameOptions)
            .headers(headers -> headers.frameOptions(fo -> fo.disable()))
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            );

        return http.build();
    }
}
