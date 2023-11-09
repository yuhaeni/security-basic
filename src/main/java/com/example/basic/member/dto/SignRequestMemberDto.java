package com.example.basic.member.dto;

import com.example.basic.member.domain.Authority;
import com.example.basic.member.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@NoArgsConstructor
@Data
public class SignRequestMemberDto {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Positive
    private int age;
    private Authority authority;


    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .age(age)
                .authority(authority)
                .build();
    }

    //비밀번호 암호화
    public void encryptPassword(PasswordEncoder passwordEncoder, String rawPassword) {
        this.password = passwordEncoder.encode(rawPassword);
    }
}
