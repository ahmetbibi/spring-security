package com.tutorial.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity // To tell spring security this is a web security configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set your configuration on the auth object
        // To use this method, we have to provide user password and role.
        auth.inMemoryAuthentication()
                .withUser("test")
                .password("test")
                .roles("user")
                .and()
                .withUser("foe")
                .password("foe")
                .roles("admin");
    }

    // Spring security expect from us a password hashing mechanism
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        // We can implement here any of password hashing methods
        // For this time we are doing nothing and inform Spring Security
        return NoOpPasswordEncoder.getInstance();
    }
}
