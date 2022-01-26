package com.ss.utopia.restapi.configs;

import com.ss.utopia.restapi.dao.UserRepository;
import com.ss.utopia.restapi.jwt.JwtAuthenticationFilter;
import com.ss.utopia.restapi.jwt.JwtAuthorizationFilter;
import com.ss.utopia.restapi.services.UserPrincipalDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    UserRepository userDB;

    @Autowired
    UserPrincipalDetailsService userPrincipalDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDB))
            .authorizeRequests()
            .antMatchers("/booking-agents").hasAnyAuthority("ADMIN", "AGENT")
            .antMatchers("/booking-users").hasAnyAuthority("ADMIN", "USER")
            .antMatchers("/booking-guests").hasAnyAuthority("ADMIN", "AGENT")
            .antMatchers(HttpMethod.POST, "/booking-guests").authenticated()
            .antMatchers("/flight-bookings").hasAnyAuthority("ADMIN", "USER", "AGENT")
            .antMatchers("/passengers").hasAnyAuthority("ADMIN", "AGENT")
            .antMatchers(HttpMethod.POST, "/passenger").authenticated()
            .antMatchers("/booking-payments").hasAnyAuthority("ADMIN", "AGENT")
            .antMatchers(HttpMethod.POST, "/booking-payments").authenticated()
            .antMatchers("/*").hasAnyAuthority("ADMIN")
            ;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
