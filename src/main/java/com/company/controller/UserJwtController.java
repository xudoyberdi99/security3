package com.company.controller;

import com.company.entity.User;
import com.company.model.LoginDto;
import com.company.security.TokenProvider;
import com.company.service.UserService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserJwtController {
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;


    @PostMapping("/auth")
    public ResponseEntity<JWTToken> authorize(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword());
        Authentication authentication= authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt=tokenProvider.createToken(authentication,loginDto.isRememberMe());
        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Authorization","Bearer "+jwt);
        return new ResponseEntity<>(new JWTToken(jwt),httpHeaders, HttpStatus.OK);
    }

    static class JWTToken{
        private  String token;
        public JWTToken(String jwt){
            this.token=token;
        }
        @JsonProperty("jwt_token")
        public String getToken() {
            return token;
        }
    }

    @PostMapping("/register")
    public ResponseEntity create(@RequestBody User user){
        User result=userService.save(user);
        return ResponseEntity.ok(result);
    }
}
