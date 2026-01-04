package be.rayanhaddou.equipmentrental.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ CSRF aan, token in cookie zodat je het met fetch kan meesturen
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()) // <-- BELANGRIJK
                        .ignoringRequestMatchers("/h2-console/**")
                )
                .authorizeHttpRequests(auth -> auth
                        // pages + static
                        .requestMatchers("/", "/catalog", "/login", "/register", "/css/**", "/js/**").permitAll()

                        .requestMatchers("/api/csrf").permitAll()

                        // auth endpoints: alleen login/register publiek
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/logout").authenticated()

                        // publieke API
                        .requestMatchers("/api/products/**").permitAll()

                        // H2 console (POC) - alleen admin
                        .requestMatchers("/h2-console/**").hasRole("ADMIN")

                        // protected
                        .requestMatchers("/cart", "/checkout", "/api/cart/**").authenticated()
                        .requestMatchers("/api/reservations/**").authenticated()

                        .anyRequest().authenticated()
                )
                // ✅ H2-console: frames enkel same-origin (niet volledig uitzetten)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .formLogin(form -> form.disable())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}