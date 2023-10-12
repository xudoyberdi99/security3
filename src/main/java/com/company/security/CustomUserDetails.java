package com.company.security;

import com.company.entity.Role;
import com.company.exp.UserNotActivateException;
import com.company.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component("userDetailService")
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String lowercaseUsername=username.toLowerCase();

        return userRepository.findByUsername(lowercaseUsername)
                .map(user -> createSpringSecurityUser(lowercaseUsername,user))
                .orElseThrow(()->new UserNotActivateException("user "+ username + "was not activated"));
    }

    private User createSpringSecurityUser(String username, com.company.entity.User user){
        if (!user.isActivated()){
            throw new UserNotActivateException("user "+ username + "was not activated");
        }
        List<GrantedAuthority> authorities = user
                .getRoles().stream().map(Role::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return new User(username,user.getPassword(),authorities);
    }
}
