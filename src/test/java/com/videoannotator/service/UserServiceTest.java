package com.videoannotator.service;

import com.videoannotator.config.jwt.JwtUtils;
import com.videoannotator.model.ConfirmToken;
import com.videoannotator.model.Role;
import com.videoannotator.model.User;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.repository.PasswordResetRepository;
import com.videoannotator.repository.RoleRepository;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.service.impl.UserServiceImpl;
import com.videoannotator.util.UserUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordResetRepository passwordResetRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    MessageSource messageSource;
    @Mock
    IEmailService emailService;
    @Mock
    Environment env;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UserUtils userUtils;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("Save user register successful")
    void savedUserOk() {
        RegisterRequest registerRequest = new RegisterRequest().setFullName("Test").setEmail("test@test.com").setPassword("12345").setPassword("12345");
        Role role = new Role();
        role.setId(1L);
        role.setRoleName("Normal");
        when(roleRepository.findById(any())).thenReturn(Optional.of(role));
        when(passwordResetRepository.save(any(ConfirmToken.class))).then(returnsFirstArg());
        User user = new User();
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);
        RegisterResponse<Long> response = userService.registerUser(registerRequest);
        assertThat(response.getId()).isEqualTo(1L);
    }
}
