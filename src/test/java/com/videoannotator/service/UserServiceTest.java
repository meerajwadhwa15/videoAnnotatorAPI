package com.videoannotator.service;

import com.videoannotator.model.Role;
import com.videoannotator.model.User;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.repository.RoleRepository;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("Save to DB successful")
    void saveUser() {
        RegisterRequest dto = new RegisterRequest();
        dto.setFullName("Match");
        dto.setEmail("match@match.com");
        dto.setPassword("password");
        User user = new User();
        user.setId(1L);
        when(userRepository.save(any())).thenReturn(user);
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("Normal");
        when(roleRepository.findById(any())).thenReturn(Optional.of(role));
        RegisterResponse<Long> registerResponse = userService.registerUser(dto);
        Assertions.assertEquals(user.getId(), registerResponse.getId());
    }
}
