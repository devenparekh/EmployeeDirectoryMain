package com.employeedirectory.UserDetails.UserDetailsController;

import com.employeedirectory.Employee.Service.JwtService;
import com.employeedirectory.UserDetails.DTO.AuthRequest;
import com.employeedirectory.UserDetails.DTO.AuthenticationResponse;
import com.employeedirectory.UserDetails.UserDetailsEntity.UserInfo;
import com.employeedirectory.UserDetails.UserDetailsService.UserInfoUserDetails;
import com.employeedirectory.UserDetails.UserDetailsService.UserInfoUserDetailsService;
import com.employeedirectory.UserDetails.UserDetailsService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserDetailsController {

    @Autowired
    UserInfoUserDetailsService userInfoUserDetailsService;

    @Autowired
    UserService service;

    @Autowired
    JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/newUser")
    public ResponseEntity<UserInfo> addNewUser(@RequestBody UserInfo userInfo) {
        try {
            service.addUser(userInfo);
            return ResponseEntity.ok( userInfo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userInfo);

    }

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            UserDetails userInfoUserDetails = userInfoUserDetailsService.loadUserByUsername(authRequest.getUsername());
            final String token = jwtService.generateToken(userInfoUserDetails);
            return token;
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }


    }
}
