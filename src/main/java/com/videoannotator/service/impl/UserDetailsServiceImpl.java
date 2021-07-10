package com.videoannotator.service.impl;


import com.videoannotator.exception.NotFoundException;
import com.videoannotator.model.User;
import com.videoannotator.model.UserDetailsImpl;
import com.videoannotator.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmailAndActive(email, true);
        if (user.isEmpty()) {
            throw new NotFoundException();
        }
        return UserDetailsImpl.build(user.get());
    }
}
