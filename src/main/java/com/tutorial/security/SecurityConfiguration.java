package com.tutorial.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity // To tell spring security this is a web security configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set your configuration on the auth object
        // To use this method, we have to provide user password and role.
        /*
        auth.inMemoryAuthentication()
                .withUser("test")
                .password("test")
                .roles("user")
                .and()
                .withUser("foe")
                .password("foe")
                .roles("admin");
         */

        // With default schema by populating the user credentials (not a real world app)
        /*
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .withDefaultSchema()
                .withUser(
                        User.withUsername("user")
                        .password("pass")
                        .roles("user")
                )
                .withUser(
                        User.withUsername("admin")
                                .password("user")
                                .roles("admin")
                );

         */

        // We're going to create the user details schema
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                // To use our own database credentials we can manipulate the related fields (if we use different schema)
                .usersByUsernameQuery("select username, password, enabled"
                        + " from users where username=?")
                .authoritiesByUsernameQuery("select username, authority "
                        + "from authorities where username=?");
    }

    // Spring security expect from us a password hashing mechanism
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        // We can implement here any of password hashing methods
        // For this time we are doing nothing and inform Spring Security
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers("/user").hasAnyRole("ADMIN", "USER")
                .antMatchers("/").permitAll()
                .and().formLogin();
    }


}
