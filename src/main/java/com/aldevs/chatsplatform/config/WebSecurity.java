package com.aldevs.chatsplatform.config;

import com.aldevs.chatsplatform.config.jwt.JWTAuthenticationEntryPoint;
import com.aldevs.chatsplatform.security.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final AuthenticationService authService;

    public WebSecurity(AuthenticationService authService) {
        this.authService = authService;
    }

    private static final String AUTH_ENDPOINT = "/api/auth/**";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests()
                    .antMatchers(AUTH_ENDPOINT).permitAll()
                    .anyRequest().authenticated()
                .and()
                    .addFilter(authenticationFilter())
                .exceptionHandling()
                    .authenticationEntryPoint(new JWTAuthenticationEntryPoint());
    }


    @Override
    protected void configure(AuthenticationManagerBuilder builder){
        builder.authenticationProvider(preAuthenticatedAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    protected RequestHeaderAuthenticationFilter authenticationFilter() throws Exception {
        RequestHeaderAuthenticationFilter filter = new RequestHeaderAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setPrincipalRequestHeader("Authorization");
        filter.setExceptionIfHeaderMissing(false);
        return filter;
    }

    @Bean
    protected AuthenticationProvider preAuthenticatedAuthenticationProvider(){
        PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
        provider.setPreAuthenticatedUserDetailsService(authService);
        return provider;
    }
}
