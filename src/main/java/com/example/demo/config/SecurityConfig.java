package com.example.demo.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * CSRF protection is enabled by default with Java Configuration.
     * If you would like to disable CSRF, the corresponding Java configuration can be seen below.
     */
/*    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
    }*/
}
