package com.videoannotator.util;

import com.videoannotator.exception.NotLoginException;
import com.videoannotator.model.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public UserDetailsImpl userDetails() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = null;
        if (authentication.getPrincipal() instanceof UserDetailsImpl) {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
        }
        if (userDetails == null) {
            throw new NotLoginException();
        }
        return userDetails;
    }
}
