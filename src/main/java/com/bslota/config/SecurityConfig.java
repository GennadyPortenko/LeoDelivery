package com.bslota.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Created by bslota on 2017-03-14.
 */
@Configuration
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class SpecialSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/special/**")
                .authorizeRequests()
                    .anyRequest()
                    .hasRole("ADMIN")
                    .and()
                .formLogin()
                    .loginPage("/special/login")
                    .defaultSuccessUrl("/special/home")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/special/logout")
                    .permitAll();
        }
    }

    @Configuration
    public static class RegularSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/regular/**")
                .authorizeRequests()
                    .anyRequest()
                    .hasRole("USER")
                    .and()
                .authorizeRequests()
                    .antMatchers("/css/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/regular/login")
                    .defaultSuccessUrl("/regular/home")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/regular/logout")
                    .permitAll();
        }
    }

    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername("user")
                .password(encoder().encode("userPass"))
                .roles("USER")
                .build());

        manager.createUser(User
                .withUsername("admin")
                .password(encoder().encode("adminPass"))
                .roles("ADMIN")
                .build());

        return manager;
    }

    @Bean
    public static PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}