package com.employeedirectory.UserDetails.UserDetailsRepository;


import com.employeedirectory.UserDetails.UserDetailsEntity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByName(String name);
    Optional<UserInfo> findByEmail(String email);
}
