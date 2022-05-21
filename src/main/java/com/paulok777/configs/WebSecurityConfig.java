package com.paulok777.configs;

import com.paulok777.entities.Role;
import com.paulok777.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/commodity_expert/products", "/commodity_expert/products/{id}")
                    .hasAuthority(Role.COMMODITY_EXPERT.getAuthority())
                .antMatchers(
                        "/senior_cashier/orders/cancel/{orderId}/{productId}",
                        "/senior_cashier/orders/cancel/{id}", "/senior_cashier/reports/x",
                        "/senior_cashier/reports/z", "/senior_cashier/orders", "/senior_cashier/orders/{id}",
                        "/senior_cashier/orders/{orderId}/{productId}", "/senior_cashier/orders/close/{id}")
                    .hasAuthority(Role.SENIOR_CASHIER.getAuthority())
                .antMatchers("/cashier/orders", "/cashier/orders/{id}",
                        "/cashier/orders/{orderId}/{productId}", "/cashier/orders/close/{id}")
                    .hasAnyAuthority(Role.CASHIER.getAuthority(), Role.SENIOR_CASHIER.getAuthority())
                .antMatchers("/logout")
                    .hasAnyAuthority(Role.CASHIER.getAuthority(), Role.SENIOR_CASHIER.getAuthority(), Role.COMMODITY_EXPERT.getAuthority())
                .antMatchers("/css/**").permitAll()
                .antMatchers("/", "/login", "/registration").anonymous()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(myAuthenticationSuccessHandler());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new UrlAuthenticationSuccessHandler();
    }
}