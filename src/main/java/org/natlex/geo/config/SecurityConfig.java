package org.natlex.geo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Arrays;
import java.util.Base64;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/22/24
 * Time: 2:37 PM
 */

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Value("${app.auth.user}")
    private String user;
    @Value("${app.auth.password}")
    private String password;
    @Value("${app.auth.roles}")
    private String[] roles;

    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                        .httpBasic(Customizer.withDefaults());

        http.authorizeHttpRequests(requests-> requests
                        .requestMatchers("/v2/api-docs/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic
                        .authenticationEntryPoint(authenticationEntryPoint)
                );

        return http.build();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser(user)
                .password(passwordEncoder().encode(new String(Base64.getDecoder().decode(password))))
                .roles(roles);
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
