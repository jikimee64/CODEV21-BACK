package com.j2kb.codev21.domains.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.j2kb.codev21.domains.user.domain.Field;
import com.j2kb.codev21.domains.user.domain.Status;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.mapper.UserMapper;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test
    @DisplayName("DTO에서 Entity로 변환하는 테스트")
    void test_dto_to_event() {
        /* given */
        UserDto.JoinReq dto = UserDto.JoinReq.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("이름")
            .joinGisu("1기")
            .githubId("jikimee64@gmail.com")
            .build();
        /* when */
        final User user = UserMapper.INSTANCE.userDtoToEntity(dto);
        /* then */
        assertNotNull(user);
        assertThat(user.getEmail()).isEqualTo("jikimee64@gmail.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("이름");
        assertThat(user.getJoinGisu()).isEqualTo("1기");
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(user.getField()).isEqualTo(Field.NONE);
        assertThat(user.getGithubId()).isEqualTo("jikimee64@gmail.com");
    }

    @Test
    @DisplayName("회원가입")
    void shouldSortedInOrderOfGrade() {
        /* given */
        UserDto.JoinReq dto = UserDto.JoinReq.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("이름")
            .joinGisu("1기")
            .githubId("jikimee64@gmail.com")
            .build();

        /* when */
        /* then */
        assertThat(userService.joinUser(dto).getId()).isEqualTo(1L);
    }


}