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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig{
    @Autowired
    private final PrincipalServiceImpl principalServiceImpl = null;
    @Autowired
    private final LoginFailHandler loginFailHandler = null;
    @Autowired
    private final DataSource dataSource = null;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests().antMatchers("/admin/*").hasRole(Role.ADMIN.name())
                .anyRequest().permitAll()
            .and()
                .formLogin()
                .loginPage("/login")
            .and()
                .logout()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID","remember-me")
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //csrf 토큰 자동 생성(요청 시 HttpServletRequest 저장)
            .and()
                .oauth2Login()
                .loginPage("/login")
                .failureHandler(loginFailHandler)
                .userInfoEndpoint()
                .userService(principalServiceImpl);
        return http.build();
    }

    @Bean //외부 DB를 세션 저장소로 사용하여 로그인 유저 정보 보관
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    //세션 저장소 연동하여 유저 정보를 보관할 때 해당 유저 정보의 직렬화가 필요한 듯하다. ATTRIBUTE_BYTES 필드에 blob 형식의 데이터가 들어가 있는 걸 확인함. 
    //member entity, principal 구현체 직렬화했으며, 하지 않으면 에러 발생했음
}