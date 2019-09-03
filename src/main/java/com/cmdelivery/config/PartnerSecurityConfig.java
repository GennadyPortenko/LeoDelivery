package com.cmdelivery.config;

import com.cmdelivery.config.component.PartnerAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@Order(2)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class PartnerSecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DataSource dataSource;
    private final PartnerAuthenticationSuccessHandler partnerAuthenticationSuccessHandler;
    @Value("${spring.queries.partner-query}")
    private String partnerQuery;
    @Value("${spring.queries.roles-partner-query}")
    private String rolesPartnerQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(partnerQuery)
                .authoritiesByUsernameQuery(rolesPartnerQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/cabinet/**")
            .authorizeRequests()
                .anyRequest()
                .hasRole("PARTNER")
                .and()
            .formLogin()
                // .usernameParameter("email")
                // .passwordParameter("password")
                .loginPage("/cabinet/login")
                .loginProcessingUrl("/cabinet/login")
                .failureUrl("/cabinet/login?error")
                .permitAll().successHandler(partnerAuthenticationSuccessHandler)
                .and()
            .logout()
                .logoutUrl("/cabinet/logout")
                .permitAll();
    }

}

