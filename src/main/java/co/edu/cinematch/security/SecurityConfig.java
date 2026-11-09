package co.edu.cinematch.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain chain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // Permite que los dos frontends locales pasen la solicitud CORS
                // antes de validar el token JWT.
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/auth/login", "/api/clientes/login", "/api/clientes/registro",
                                "/api/pagos/webhook", "/api/estado")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/peliculas/**", "/api/cines/**", "/api/funciones/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/peliculas/**", "/api/cines/**", "/api/funciones/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/peliculas/**", "/api/cines/**", "/api/funciones/**")
                        .hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/peliculas/**", "/api/cines/**", "/api/funciones/**")
                        .hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
