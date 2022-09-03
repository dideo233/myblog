package com.mycustomblog.blog.config;

import com.mycustomblog.blog.config.exception.LoginFailHandler;
import com.mycustomblog.blog.domain.Role;
import com.mycustomblog.blog.service.PrincipalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig{
    @Autowired
    private final PrincipalServiceImpl principalServiceImpl = null;
    @Autowired
    private final LoginFailHandler loginFailHandler = null;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable(); //토큰 X
        http.authorizeHttpRequests().antMatchers("/admin/*").hasRole(Role.ADMIN.name())
                .anyRequest().permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
            .and()
                .logout()
                .logoutSuccessUrl("/")
            .and()
                .oauth2Login()
                .loginPage("/login")
                .failureHandler(loginFailHandler)
                .userInfoEndpoint()
                .userService(principalServiceImpl);
        return http.build();
    }
}