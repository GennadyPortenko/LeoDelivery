package com.bslota.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.bslota.config.component.PersonAuthenticationSuccessHandler;

import javax.sql.DataSource;

@Configuration
@Order(1)
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class RegularSecurityConfig extends WebSecurityConfigurerAdapter {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DataSource dataSource;
    private final PersonAuthenticationSuccessHandler personAuthenticationSuccessHandler;
    @Value("${spring.queries.person-query}")
    private String personQuery;
    @Value("${spring.queries.roles-person-query}")
    private String rolesPersonQuery;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
            jdbcAuthentication()
            .usersByUsernameQuery(personQuery)
            .authoritiesByUsernameQuery(rolesPersonQuery)
            .dataSource(dataSource)
            .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // .csrf().disable()
            .antMatcher("/regular/**")
            .authorizeRequests()
                .anyRequest()
                .hasRole("USER")
                .and()
                /*
            .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .anyRequest().authenticated()
                .and()
                */
            .formLogin()
                .loginPage("/regular/login")
                .loginProcessingUrl("/regular/login")
                .defaultSuccessUrl("/regular/home")
                .failureUrl("/regular/login?error")
                .permitAll().successHandler(personAuthenticationSuccessHandler)
                .and()
            .logout()
                .logoutUrl("/regular/logout")
                .permitAll();
    }

    /*
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/css/**", "/fonts/**", "/img/**", "/js/**", "/vendor/**");
    }
    */

}

