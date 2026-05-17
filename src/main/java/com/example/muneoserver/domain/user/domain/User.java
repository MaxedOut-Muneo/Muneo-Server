package com.example.muneoserver.domain.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.example.muneoserver.domain.shared.BaseTimeEntity;

@Getter
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_user_provider", columnNames = {"auth_provider", "provider_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String email;

    @Column
    private String password;

    @Column(length = 20)
    private String name;

    @Column(length = 13)
    private String phoneNumber;

    @Column
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AuthProvider authProvider;

    @Column(length = 100)
    private String providerId;

    @Column
    private String profileImageUrl;

    @Column(nullable = false)
    private boolean profileCompleted;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    private User(
            String email,
            String encodedPassword,
            String name,
            String phoneNumber,
            LocalDate birthDate,
            AuthProvider authProvider,
            String providerId,
            String profileImageUrl,
            boolean profileCompleted,
            boolean emailVerified,
            UserRole role
    ) {
        this.email = email;
        this.password = encodedPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.authProvider = authProvider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.profileCompleted = profileCompleted;
        this.emailVerified = emailVerified;
        this.deleted = false;
        this.role = role;
    }

    public static User create(String email, String encodedPassword, String name, String phoneNumber, LocalDate birthDate) {
        return new User(
                email,
                encodedPassword,
                name,
                phoneNumber,
                birthDate,
                AuthProvider.LOCAL,
                null,
                null,
                true,
                false,
                UserRole.USER
        );
    }

    public static User createSocialKakao(String providerId, String nickname, String profileImageUrl, String email) {
        return new User(
                email,
                null,
                nickname,
                null,
                null,
                AuthProvider.KAKAO,
                providerId,
                profileImageUrl,
                false,
                true,
                UserRole.USER
        );
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void completeSocialSignup(String name, String phoneNumber, LocalDate birthDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.profileCompleted = true;
    }

    public void updateLocalProfile(String email, String name, String phoneNumber, LocalDate birthDate) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public void updateSocialProfile(String name, String phoneNumber, LocalDate birthDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public void updateByAdmin(String email, String name, String phoneNumber, LocalDate birthDate) {
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }

    public void changeRole(UserRole role) {
        this.role = role;
    }

    public void markEmailVerified() {
        this.emailVerified = true;
    }

    public void withdraw() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }
}
