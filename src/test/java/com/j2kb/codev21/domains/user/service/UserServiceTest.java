package com.j2kb.codev21.domains.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.j2kb.codev21.domains.user.domain.Field;
import com.j2kb.codev21.domains.user.domain.Status;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.SelectUserRes;
import com.j2kb.codev21.domains.user.dto.UserDto.UpdateUserByAdminReq;
import com.j2kb.codev21.domains.user.dto.UserDto.UpdateUserReq;
import com.j2kb.codev21.domains.user.dto.UserDto.UserIdRes;
import com.j2kb.codev21.domains.user.dto.mapper.UserMapper;
import com.j2kb.codev21.domains.user.exception.MemberNotFoundException;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    private UserDto.JoinReq dto;

    @BeforeEach
    public void before() {
        dto = UserDto.JoinReq.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("이름")
            .joinGisu("1기")
            .githubId("jikimee64@gmail.com")
            .build();
    }

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

        /* when */

        /* then */
        assertThat(userService.joinUser(dto).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("회원 전체 조회")
    void all_select_member() {
        /* given */
        userService.joinUser(dto);

        /* when */
        List<SelectUserRes> userList = userService.getUserList();

        /* then */
        userList.forEach(s ->
            assertAll(
                () -> assertNotNull(s.getId()),
                () -> assertNotNull(s.getEmail()),
                () -> assertNotNull(s.getName()),
                () -> assertNotNull(s.getJoinGisu()),
                () -> assertNotNull(s.getStatus()),
                () -> assertNotNull(s.getField()),
                () -> assertNotNull(s.getGithubId()),
                () -> assertNotNull(s.getCreatedAt())
            )
        );
    }

    @Test
    @DisplayName("회원 단건 조회")
    void select_member() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(dto);

        /* when */
        SelectUserRes selectUserRes = userService.getUser(userIdRes.getId());

        /* then */
        assertAll(
            () -> assertNotNull(selectUserRes.getId()),
            () -> assertNotNull(selectUserRes.getEmail()),
            () -> assertNotNull(selectUserRes.getName()),
            () -> assertNotNull(selectUserRes.getJoinGisu()),
            () -> assertNotNull(selectUserRes.getStatus()),
            () -> assertNotNull(selectUserRes.getField()),
            () -> assertNotNull(selectUserRes.getGithubId()),
            () -> assertNotNull(selectUserRes.getCreatedAt())
        );
    }

    @Test
    @DisplayName("회원수정(유저권한)")
    void update_member_by_admin() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(dto);
        UpdateUserReq dto = UpdateUserReq.builder()
            .password("1q2w3e4r1!")
            .build();

        /* when */
        SelectUserRes selectUserRes = userService.updateUser(userIdRes.getId(), dto);

        /* then */
        assertAll(
            () -> assertNotNull(selectUserRes.getId()),
            () -> assertNotNull(selectUserRes.getEmail()),
            () -> assertNotNull(selectUserRes.getName()),
            () -> assertNotNull(selectUserRes.getJoinGisu()),
            () -> assertNotNull(selectUserRes.getStatus()),
            () -> assertNotNull(selectUserRes.getField()),
            () -> assertNotNull(selectUserRes.getGithubId()),
            () -> assertNotNull(selectUserRes.getCreatedAt())
        );
    }


    @Test
    @DisplayName("회원수정(관리자 권한)")
    void member_update_by_admin() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(dto);
        UserDto.UpdateUserByAdminReq dto = UserDto.UpdateUserByAdminReq.builder()
            .status("INACTIVE")
            .field("BACK_END")
            .joinGisu("3기")
            .build();

        /* when */
        SelectUserRes selectUserRes = userService.updateUserByAdmin(userIdRes.getId(), dto);

        /* then */
        assertAll(
            () -> assertNotNull(selectUserRes.getId()),
            () -> assertNotNull(selectUserRes.getEmail()),
            () -> assertNotNull(selectUserRes.getName()),
            () -> assertNotNull(selectUserRes.getJoinGisu()),
            () -> assertNotNull(selectUserRes.getStatus()),
            () -> assertNotNull(selectUserRes.getField()),
            () -> assertNotNull(selectUserRes.getGithubId()),
            () -> assertNotNull(selectUserRes.getCreatedAt())
        );
    }

    @Test
    @DisplayName("회원삭제")
    void member_delete() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(dto);

        /* when */
        userService.deleteUser(userIdRes.getId());

        /* then */
        assertThrows(MemberNotFoundException.class,
            () -> userService.getUser(1L));
    }


}