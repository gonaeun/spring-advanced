package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
class PasswordEncoderTest {

    @InjectMocks
    private PasswordEncoder passwordEncoder;

    @Test
    void matches_메서드가_정상적으로_동작한다() {
        // given 준비 : 비밀번호 암호화
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // when 행동 : 비밀번호 일치하는지 검사
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword); // 파라미터 순서 올바르게 변경

        // then 검증 : 일치한다면 true
        assertTrue(matches);
    }
}
