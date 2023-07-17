package com.employeedirectory.UserDetails.UserDetailsService;

import com.employeedirectory.UserDetails.UserDetailsEntity.UserInfo;
import com.employeedirectory.UserDetails.UserDetailsRepository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserInfoRepository repository;

    public void addUser(UserInfo userInfo) {
        Optional<UserInfo> entryByEmail = repository.findByEmail(userInfo.getEmail());

            if (userInfo.getEmail().isEmpty()) {
                throw new NullPointerException("Email cannot be Empty");
            } else if (entryByEmail.isPresent()) {
                System.out.println("Email Id is already present. Use Another Email or go to Login.");
                throw new IllegalStateException("Email Already present");
            }
            if (userInfo.getName().isEmpty()) {
                System.out.println("Name cannot be Empty.");
                throw new NullPointerException("Please provide a Name.");
            }
            if (userInfo.getPassword().isEmpty()) {
                System.out.println("Password cannot be Empty.");
                throw new NullPointerException("Please provide a Password.");
            }
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        System.out.println("User Details saved to Database Successfully!");
    }
}

