package com.security.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final UserDetailsService userDetailsService;

    public WebSecurity(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // authenticating all the request with basic authentication
//        httpSecurity.authorizeHttpRequests(
//                request -> request.anyRequest().authenticated()
//        ).formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());
        // we can also disable csrf using this filter chain but in case the default behaviour require csrf for the post request aur agar csrf token ko disable kar diye toh then we can make post request without csrf token
//        httpSecurity.csrf(csrf -> csrf.disable()).authorizeHttpRequests(
//                request -> request.anyRequest().authenticated()
//        ).formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());

        //if any request coming from register api then it will not be authenticated because register karne mein why do we authenticate

        httpSecurity.csrf(csrf -> csrf.disable()).authorizeHttpRequests(
                request -> request.requestMatchers("register","login").permitAll().anyRequest().authenticated()
        ).httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        //we haven't applied password encoder below so it will not work
//        UserDetails talib = User.withUsername("talib").password("talib").roles("USER").build();
//        UserDetails zaid = User.withUsername("zaid").password("zaid").roles("USER").build();

        // this will work without password encoder bcz we are using {noop} wo if the password is in the plain text it will work out without password encoder
        UserDetails talib = User.withUsername("talib").password("{noop}talib").roles("USER").build();
        UserDetails zaid = User.withUsername("zaid").password("{noop}zaid").roles("USER").build();

        return new InMemoryUserDetailsManager(talib,zaid);

    }
//
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder( bCryptPasswordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
       return configuration.getAuthenticationManager();
    }
}
