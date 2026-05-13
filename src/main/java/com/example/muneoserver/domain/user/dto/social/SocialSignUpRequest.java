package com.example.muneoserver.domain.user.dto.social;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record SocialSignUpRequest(
        @NotBlank(message = "소셜 회원가입 티켓은 필수입니다.")
        String ticket,

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 20, message = "이름은 최대 20자입니다.")
        String name,

        @NotBlank(message = "연락처는 필수입니다.")
        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "ex) 010-0000-0000 형식입니다.")
        String phoneNumber,

        @NotNull(message = "생년월일은 필수입니다.")
        @PastOrPresent(message = "미래 날짜는 작성할 수 없습니다.")
        LocalDate birthDate
) {
}
