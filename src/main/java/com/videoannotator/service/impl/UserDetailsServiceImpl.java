package com.videoannotator.service.impl;


import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.UserDetailsImpl;
import com.videoannotator.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmailAndActive(email, true).orElseThrow(NotFoundException::new);
        var userDetails = new UserDetailsImpl();
        userDetails.setId(user.getId())
                .setEmail(user.getEmail())
                .setFullName(user.getFullName())
                .setPassword(user.getPassword())
                .setRoleId(user.getRole().getId())
                .setAuthorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleName())))
                .setAddress(user.getAddress())
                .setPhone(user.getPhone())
                .setIntroduction(user.getIntroduction())
                .setAvatar(user.getAvatar());
        return userDetails;
    }
}
