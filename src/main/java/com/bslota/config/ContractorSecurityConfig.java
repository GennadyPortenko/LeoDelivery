package com.bslota.config;

import com.bslota.config.component.ContractorAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
class ContractorSecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DataSource dataSource;
    private final ContractorAuthenticationSuccessHandler contractorAuthenticationSuccessHandler;
    @Value("${spring.queries.contractor-query}")
    private String contractorQuery;
    @Value("${spring.queries.roles-contractor-query}")
    private String rolesContractorQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                jdbcAuthentication()
                .usersByUsernameQuery(contractorQuery)
                .authoritiesByUsernameQuery(rolesContractorQuery)
                .dataSource(dataSource)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/contractor/**")
            .authorizeRequests()
                // .antMatchers("/contractor/cabinet").permitAll() //hasRole("CONTRACTOR")
                .anyRequest()
                .hasRole("CONTRACTOR")
                .and()
            .formLogin()
                .loginPage("/contractor/login")
                .loginProcessingUrl("/contractor/login")
                .defaultSuccessUrl("/contractor/cabinet")
                .failureUrl("/contractor/login?error")
                .permitAll().successHandler(contractorAuthenticationSuccessHandler)
                .and()
            .logout()
                .logoutUrl("/contractor/logout")
                .permitAll();
    }

}

