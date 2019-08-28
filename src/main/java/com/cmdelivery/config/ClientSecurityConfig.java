package com.cmdelivery.config;

import com.cmdelivery.config.component.ClientAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.cmdelivery.config.component.ClientAuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@Order(1)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ClientSecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DataSource dataSource;
    private final ClientAuthenticationSuccessHandler clientAuthenticationSuccessHandler;
    @Value("${spring.queries.client-query}")
    private String clientQuery;
    @Value("${spring.queries.roles-client-query}")
    private String rolesClientQuery;


    @Autowired
    private ClientAuthenticationProvider clientAuthProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(clientAuthProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/food/**")
            .authorizeRequests()
                .anyRequest()
                .permitAll() // .hasRole("USER")
                .and()
            .formLogin()
                .loginPage("/food/login")
                .loginProcessingUrl("/food/login")
                .usernameParameter("phone")
                .passwordParameter("password")
                .failureUrl("/food/login?error")
                .permitAll().successHandler(clientAuthenticationSuccessHandler)
                .and()
            .logout()
                .logoutUrl("/food/logout")
                .logoutSuccessUrl("/food")
                .permitAll();
    }

}

