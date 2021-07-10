package com.videoannotator.service.impl;

import com.videoannotator.config.jwt.JwtUtils;
import com.videoannotator.constant.RoleEnum;
import com.videoannotator.exception.EmailAlreadyExistException;
import com.videoannotator.exception.NotFoundException;
import com.videoannotator.exception.NotLoginException;
import com.videoannotator.exception.PermissionDeniedException;
import com.videoannotator.model.User;
import com.videoannotator.model.UserDetailsImpl;
import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.response.LoginResponse;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.model.response.UserResponse;
import com.videoannotator.repository.RoleRepository;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public RegisterResponse<Long> registerUser(RegisterRequest registerRequest) {
        Optional<User> userExisted = userRepository.findByEmailAndActive(registerRequest.getEmail(), true);
        if (userExisted.isPresent()) {
            throw new EmailAlreadyExistException();
        }
        var user = new User();
        user.setFullName(registerRequest.getFullName());
        user.setEmail(registerRequest.getEmail());
        user.setRole(roleRepository.findById(RoleEnum.NORMAL.getValue()).orElseThrow(NotFoundException::new));
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setActive(true);
        var userSaved = userRepository.save(user);
        var response = new RegisterResponse<Long>();
        response.setId(userSaved.getId());
        return response;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LoginResponse("Bearer " + jwtToken, userDetails.getId(), userDetails.getEmail(), userDetails.getName(), roles);
    }

    @Override
    public LoginResponse currentUser() {
        UserDetailsImpl userDetails = userDetails();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new LoginResponse(null, userDetails.getId(), userDetails.getEmail(), userDetails.getName(), roles);
    }

    @Override
    public List<UserResponse> listUser() {
        UserDetailsImpl userDetails = userDetails();
        List<UserResponse> userResponses = new LinkedList<>();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            var role = roleRepository.findById(userDetails.getRoleId()).orElseThrow(NotFoundException::new);
            List<User> userList = userRepository.findAllByRoleIsNotAndActive(role, true);
            userList.forEach(user -> {
                var response = new UserResponse();
                response.setId(user.getId());
                response.setFullName(user.getFullName());
                response.setEmail(user.getEmail());
                userResponses.add(response);
            });
            return userResponses;
        } else {
            throw new PermissionDeniedException();
        }
    }

    private UserDetailsImpl userDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        if (userDetails == null) {
            throw new NotLoginException();
        }
        return userDetails;
    }
}
