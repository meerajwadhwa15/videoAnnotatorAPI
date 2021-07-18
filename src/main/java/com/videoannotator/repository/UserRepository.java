package com.videoannotator.repository;


import com.videoannotator.model.Role;
import com.videoannotator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndActive(String email, boolean isActive);

    List<User> findAllByRoleIsNotAndActive(Role role, boolean isActive);

    List<User> findAllByRoleIsNotAndIdInAndActive(Role role, List<Long> id, boolean isActive);
}
