package com.example.userapp.auth

import com.example.userapp.config.PasswordConfig
import com.example.userapp.domain.user.service.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val passwordConfig: PasswordConfig,
    private val customUserDetailsService: CustomUserDetailsService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { csrf -> csrf.disable() }
        .authorizeHttpRequests { auth ->
            auth.requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()
            auth.requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
            auth.anyRequest().permitAll()
        }
        .httpBasic(Customizer.withDefaults())
        .build()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider = DaoAuthenticationProvider().apply {
        setUserDetailsService(customUserDetailsService)
        setPasswordEncoder(passwordConfig.passwordEncoder())
    }
}
