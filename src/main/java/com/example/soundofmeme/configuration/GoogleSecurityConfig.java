package com.example.soundofmeme.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class GoogleSecurityConfig {

    @Bean(name = "googleSecurityFilterChain")
    SecurityFilterChain googleSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2Login -> oauth2Login
                .loginPage("/oauth2/authorization/google")
                .defaultSuccessUrl("/loginSuccess", true)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler("/loginFailure"))
            );
        return http.build();
    }

}

