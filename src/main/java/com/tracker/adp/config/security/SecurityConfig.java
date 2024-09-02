package com.tracker.adp.config.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthEntryPoint authEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final PasswordEncoder passwordEncoder;



    @Bean
    @Order(1)
    SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                //.sessionManagement(se->se.maximumSessions(1).maxSessionsPreventsLogin(true).sessionRegistry(sessionRegistry()))
                //.exceptionHandling(exp->exp.authenticationEntryPoint(appAuthEntryPoint).accessDeniedHandler(accessDeniedHandler))
                .formLogin(form -> form.loginPage("/users/login").defaultSuccessUrl("/users/home", true).permitAll())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users/registration").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/static/favicon.ico").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(c -> c.accessDeniedPage("/403"))
                .logout(out -> out.logoutSuccessUrl("/users/login?logout").deleteCookies().clearAuthentication(true).invalidateHttpSession(true))
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


}
