package com.security.spring_security.service;


import com.security.spring_security.CustomUserDetail;
import com.security.spring_security.entity.User;
import com.security.spring_security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByUsername(username);
        if(Objects.isNull(user)){
            System.out.println("user unavailable");
            throw new UsernameNotFoundException("user not found");
        }

        return new CustomUserDetail(user);
    }
}