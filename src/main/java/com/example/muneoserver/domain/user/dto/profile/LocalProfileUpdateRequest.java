package com.example.muneoserver.domain.user.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record LocalProfileUpdateRequest(
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 20, message = "이름은 최대 20자입니다.")
        String name,

        @NotBlank(message = "연락처는 필수입니다.")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "ex) 010-0000-0000 형식입니다.")
        String phoneNumber,

        @NotNull(message = "생년월일은 필수입니다.")
        @PastOrPresent(message = "미래 날짜는 작성할 수 없습니다.")
        LocalDate birthDate,

        @Size(min = 8, max = 100, message = "비밀번호는 8자 이상 100자 이하로 입력해주세요.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "비밀번호는 최소 1개의 영문자와 숫자를 포함해야 합니다.")
        String newPassword,

        String newPasswordConfirm
) {
}
