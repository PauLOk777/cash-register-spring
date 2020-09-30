package com.paulok777.config;

import com.paulok777.entity.Role;
import com.paulok777.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/products", "/products/{id}")
                .hasAuthority(Role.COMMODITY_EXPERT.getAuthority())
                .antMatchers("/orders/products/cancel", "/orders/cancel", "/reports/x", "/reports/z")
                .hasAuthority(Role.SENIOR_CASHIER.getAuthority())
                .antMatchers("/orders", "/orders/products", "/orders/products/{id}", "orders/close/{id}")
                .hasAnyAuthority(Role.CASHIER.getAuthority(), Role.SENIOR_CASHIER.getAuthority())
                .antMatchers("/logout")
                .hasAnyAuthority(Role.CASHIER.getAuthority(), Role.SENIOR_CASHIER.getAuthority(), Role.COMMODITY_EXPERT.getAuthority())
                .antMatchers("/", "/login", "/registration").anonymous()
                .and()
                .formLogin()
                .loginPage("/login");
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}