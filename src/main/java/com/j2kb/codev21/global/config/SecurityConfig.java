package com.j2kb.codev21.global.config;

import com.j2kb.codev21.global.jwt.JwtAccessDeniedHandler;
import com.j2kb.codev21.global.jwt.JwtAuthenticationEntryPoint;
import com.j2kb.codev21.global.jwt.JwtSecurityConfig;
import com.j2kb.codev21.global.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
        JwtTokenProvider tokenProvider,
        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
        JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .disable() // rest api 이므로 기본설정 사용안함.
            // token을 사용하는 방식이기 때문에 csrf
            .csrf().disable() //csrf 방지


            .exceptionHandling()
            //인증 또는 인가에 실패한 경우 Exception 처리
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)


            // enable h2-console
            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            // 세션을 사용하지 않기 때문에 STATELESS로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/**").permitAll()
//            .antMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
//            .antMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
//            .antMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
//            .antMatchers(HttpMethod.GET, "/api/v1/auth/**").permitAll()
            //.antMatchers("/api/v1/members/**").permitAll()

            //인증을 반드시 통과해야하며, 인가(USER 권한)이 있는 사용자만 접근 가능
            //.antMatchers("/api/v1/users/**").hasAnyAuthority(Role.USER.getCode())

            .anyRequest().authenticated()


            .and()
            //사용자의 모든 요청은 JWT 필터를 통과하는 설정
            .apply(new JwtSecurityConfig(tokenProvider));
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //해당 경로의 파일들은 Spring Security가 무시할 수 있도록 설정

        // h2-console을 사용하기 위한설정
        web
            .ignoring()
            .antMatchers(
                "/h2-console/**",
                "/favicon.ico"

            );
    }
}

