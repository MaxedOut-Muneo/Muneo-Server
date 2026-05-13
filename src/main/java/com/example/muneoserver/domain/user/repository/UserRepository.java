package com.example.muneoserver.domain.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.muneoserver.domain.user.domain.AuthProvider;
import com.example.muneoserver.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndAuthProvider(String email, AuthProvider authProvider);

    Optional<User> findByAuthProviderAndProviderId(AuthProvider authProvider, String providerId);
}
