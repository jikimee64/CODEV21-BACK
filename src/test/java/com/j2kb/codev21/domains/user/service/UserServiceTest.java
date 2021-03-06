package com.j2kb.codev21.domains.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.j2kb.codev21.domains.user.domain.Field;
import com.j2kb.codev21.domains.user.domain.Status;
import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.JoinReq;
import com.j2kb.codev21.domains.user.dto.UserDto.SelectUserRes;
import com.j2kb.codev21.domains.user.dto.UserDto.UpdateUserByAdminReq;
import com.j2kb.codev21.domains.user.dto.UserDto.UpdateUserReq;
import com.j2kb.codev21.domains.user.dto.UserDto.UserIdRes;
import com.j2kb.codev21.domains.user.dto.mapper.UserMapper;
import com.j2kb.codev21.domains.user.exception.MemberDuplicationException;
import com.j2kb.codev21.domains.user.exception.MemberNotFoundException;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import com.j2kb.codev21.domains.vote.dto.VoteDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private UserDto.JoinReq dto;

    @BeforeEach
    public void before() {
        dto = JoinReq.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("??????")
            .joinGisu("1???")
            .githubId("jikimee64@gmail.com")
            .build();
    }

    @Test
    @DisplayName("DTO?????? Entity??? ???????????? ?????????")
    void test_dto_to_event() {
        /* given */
        UserDto.JoinReq dto = UserDto.JoinReq.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("??????")
            .joinGisu("1???")
            .githubId("jikimee64@gmail.com")
            .build();
        /* when */
        final User user = UserMapper.INSTANCE.joinDtoToEntity(dto);
        /* then */
        assertNotNull(user);
        assertThat(user.getEmail()).isEqualTo("jikimee64@gmail.com");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getName()).isEqualTo("??????");
        assertThat(user.getJoinGisu()).isEqualTo("1???");
        assertThat(user.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(user.getField()).isEqualTo(Field.NONE);
        assertThat(user.getGithubId()).isEqualTo("jikimee64@gmail.com");
    }

    @Test
    @DisplayName("????????????")
    void shouldSortedInOrderOfGrade() {
        /* given */

        /* when */
        UserIdRes res = userService.joinUser(userMapper.joinDtoToEntity(dto));

        /* then */
        assertNotNull(res);
    }

    @Test
    @DisplayName("???????????? ?????????")
    void duplicate_user() {
        //given
        User user1 = User.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("??????")
            .joinGisu("1???")
            .githubId("jikimee64@gmail.com")
            .build();

        User user2 = User.builder()
            .email("jikimee64@gmail.com")
            .password("password")
            .name("??????")
            .joinGisu("1???")
            .githubId("jikimee64@gmail.com")
            .build();

        //when
        userService.joinUser(user1);

        //then
        MemberDuplicationException e = assertThrows(MemberDuplicationException.class, () -> userService.joinUser(user2));
        assertThat(e.getMessage()).isEqualTo("????????? ??????????????????.");
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void all_select_member() {
        /* given */
        userService.joinUser(userMapper.joinDtoToEntity(dto));

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
    @DisplayName("?????? ?????? ??????")
    void select_member() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(userMapper.joinDtoToEntity(dto));

        log.info("id : " + userIdRes.getId());

        /* when */
        SelectUserRes selectUserRes = userService.getUser(userIdRes.getId());

        log.info("gd33 " + selectUserRes.getEmail());
        log.info("gd " + selectUserRes.getStatus());
        log.info("gd2 " + selectUserRes.getField());

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
    @DisplayName("????????????(????????????)")
    void update_member_by_admin() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(userMapper.joinDtoToEntity(dto));
//        UpdateUserReq dto = UpdateUserReq.builder()
//            .password("1q2w3e4r1!")
//            .build();

        User user = User.builder()
            .password("1q2w3e4r1!")
            .build();

        /* when */
        SelectUserRes selectUserRes = userService.updateUser(userIdRes.getId(), user);

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
    @DisplayName("????????????(????????? ??????)")
    void member_update_by_admin() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(userMapper.joinDtoToEntity(dto));
        UserDto.UpdateUserByAdminReq dto = UserDto.UpdateUserByAdminReq.builder()
            .status("INACTIVE")
            .field("BACK_END")
            .joinGisu("3???")
            .build();

        User user = User.builder()
            .status(Status.INACTIVE)
            .field(Field.BACK_END)
            .joinGisu("3???")
            .build();

        /* when */
        SelectUserRes selectUserRes = userService.updateUserByAdmin(userIdRes.getId(), user);

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
    @DisplayName("????????????")
    void member_delete() {
        /* given */
        UserIdRes userIdRes = userService.joinUser(userMapper.joinDtoToEntity(dto));

        /* when */
        userService.deleteUser(userIdRes.getId());

        /* then */
        assertThrows(MemberNotFoundException.class,
            () -> userService.getUser(userIdRes.getId()));
    }


}