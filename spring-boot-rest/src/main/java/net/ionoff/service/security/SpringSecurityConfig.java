package net.ionoff.service.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    
    private UserSecurityExceptionHandler userSecurityExceptionHandler;
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .defaultAuthenticationEntryPointFor(
                (request, response, e) -> userSecurityExceptionHandler.handleException(request, response, e),
                new AntPathRequestMatcher("/**"))
            .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/books/**").hasAuthority("ROLE_USER")
                .antMatchers(HttpMethod.POST, "/books").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/books/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PATCH, "/books/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/books/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(new UserAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
    
}
