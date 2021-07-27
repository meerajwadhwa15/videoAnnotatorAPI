package com.videoannotator.service.impl;

import com.videoannotator.config.jwt.JwtUtils;
import com.videoannotator.constant.RoleEnum;
import com.videoannotator.exception.*;
import com.videoannotator.model.ConfirmToken;
import com.videoannotator.model.Mail;
import com.videoannotator.model.User;
import com.videoannotator.model.UserDetailsImpl;
import com.videoannotator.model.mapper.ObjectMapper;
import com.videoannotator.model.request.LoginRequest;
import com.videoannotator.model.request.PasswordRequest;
import com.videoannotator.model.request.RegisterRequest;
import com.videoannotator.model.request.UpdateUserRequest;
import com.videoannotator.model.response.LoginResponse;
import com.videoannotator.model.response.RegisterResponse;
import com.videoannotator.model.response.UserDetailResponse;
import com.videoannotator.model.response.UserListResponse;
import com.videoannotator.repository.PasswordResetRepository;
import com.videoannotator.repository.RoleRepository;
import com.videoannotator.repository.UserRepository;
import com.videoannotator.service.IEmailService;
import com.videoannotator.service.IUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private static final String SUCCESS = "message.success";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;
    private final IEmailService emailService;
    private final Environment env;
    private final JwtUtils jwtUtils;

    @Override
    public RegisterResponse<Long> registerUser(RegisterRequest registerRequest) {
        Optional<User> userExisted = userRepository.findByEmailAndActive(registerRequest.getEmail(), true);
        if (userExisted.isPresent()) {
            throw new EmailAlreadyExistException();
        }
        User user = ObjectMapper.INSTANCE.registerRequestToUser(registerRequest, roleRepository, passwordEncoder);
        var userSaved = userRepository.save(user);
        final var token = UUID.randomUUID().toString();
        createToken(user, token);
        constructConfirmEmail(token, user);
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
        LoginResponse response = ObjectMapper.INSTANCE.userDetailToLoginResponse(userDetails);
        response.setToken("Bearer " + jwtToken);
        response.setRoles(roles);
        return response;
    }

    @Override
    public UserDetailResponse currentUser() {
        UserDetailsImpl userDetails = userDetails();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        UserDetailResponse response = ObjectMapper.INSTANCE.userDetailToDetailResponse(userDetails);
        response.setRoles(roles);
        return response;
    }

    @Override
    public List<UserListResponse> listUser() {
        UserDetailsImpl userDetails = userDetails();
        List<UserListResponse> userListRespons = new LinkedList<>();
        if (RoleEnum.ADMIN.getValue() == userDetails.getRoleId()) {
            var role = roleRepository.findById(userDetails.getRoleId()).orElseThrow(NotFoundException::new);
            List<User> userList = userRepository.findAllByRoleIsNotAndActive(role, true);
            userList.forEach(user -> {
                UserListResponse response = ObjectMapper.INSTANCE.userToListResponse(user);
                userListRespons.add(response);
            });
            return userListRespons;
        } else {
            throw new PermissionDeniedException();
        }
    }

    @Override
    public String resetPassword(String email) {
        var user = userRepository.findByEmailAndActive(email, true).orElseThrow(NotFoundException::new);
        final var token = UUID.randomUUID().toString();
        createToken(user, token);
        constructResetTokenEmail(token, user);
        return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
    }

    @Override
    public String newPassword(String token, PasswordRequest passwordRequest) {
        var passwordReset = passwordResetRepository.findByToken(token).orElseThrow(NotFoundException::new);
        var cal = Calendar.getInstance();
        if (passwordReset.getExpiryDate().after(cal.getTime())) {
            var user = passwordReset.getUser();
            user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
            userRepository.save(user);
        } else {
            throw new TokenExpiredException();
        }
        passwordResetRepository.delete(passwordReset);
        return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
    }

    @Override
    public String changePassword(PasswordRequest passwordRequest) {
        UserDetailsImpl userDetails = userDetails();
        if (passwordEncoder.matches(passwordRequest.getOldPassword(), userDetails.getPassword())) {
            var user = userRepository.findByEmailAndActive(userDetails.getEmail(), true).orElseThrow(NotFoundException::new);
            user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
            userRepository.save(user);
        } else {
            throw new PasswordIncorrectException();
        }
        return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
    }

    @Override
    public String verifyToken(String token) {
        var passwordReset = passwordResetRepository.findByToken(token).orElseThrow(NotFoundException::new);
        var cal = Calendar.getInstance();
        if (passwordReset.getExpiryDate().after(cal.getTime())) {
            return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
        } else {
            throw new TokenExpiredException();
        }
    }

    @Override
    public UserDetailResponse updateUser(UpdateUserRequest userRequest) {
        UserDetailsImpl userDetails = userDetails();
        var user = userRepository.findByEmailAndActive(userDetails.getEmail(), true).orElseThrow(NotFoundException::new);
        ObjectMapper.INSTANCE.updateRequestToUser(user, userRequest);
        var userSaved = userRepository.save(user);
        return new UserDetailResponse(userSaved.getId(), userSaved.getEmail(), userSaved.getFullName(), userSaved.getAddress(),
                userSaved.getPhone(), userSaved.getIntroduction(), Collections.singletonList(userSaved.getRole().getRoleName()));
    }

    @Override
    public String confirmEmail(String token) {
        var confirmToken = passwordResetRepository.findByToken(token).orElseThrow(NotFoundException::new);
        var cal = Calendar.getInstance();
        if (confirmToken.getExpiryDate().after(cal.getTime())) {
            User user = userRepository.findById(confirmToken.getUser().getId()).orElseThrow(NotFoundException::new);
            user.setActive(true);
            userRepository.save(user);
            passwordResetRepository.delete(confirmToken);
            return messageSource.getMessage(SUCCESS, null, LocaleContextHolder.getLocale());
        } else {
            throw new TokenExpiredException();
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

    private void createToken(final User user, final String token) {
        var myToken = new ConfirmToken(token, user);
        passwordResetRepository.save(myToken);
    }

    private void constructResetTokenEmail(final String token, final User user) {
        final String url = env.getProperty("url.frontend") + "/admin/reset-password?token=" + token;
        final String subject = messageSource.getMessage("message.reset.password", null, LocaleContextHolder.getLocale());
        sendMail(url, subject, user.getEmail(), "resetPassword");
    }

    private void constructConfirmEmail(final String token, final User user) {
        final String url = env.getProperty("url.frontend") + "/admin/email-confirmation?token=" + token;
        final String subject = messageSource.getMessage("message.confirm.email", null, LocaleContextHolder.getLocale());
        sendMail(url, subject, user.getEmail(), "confirmEmail");
    }

    private void sendMail(String url, String subject, String userMail, String type) {
        var mail = new Mail();
        mail.setSubject(subject);
        mail.setSendTo(userMail);
        mail.setUrl(url);
        try {
            emailService.sendMail(mail, type);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }
}
