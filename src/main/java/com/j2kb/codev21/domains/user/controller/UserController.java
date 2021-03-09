package com.j2kb.codev21.domains.user.controller;

import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.DeleteUserCheckRes;
import com.j2kb.codev21.domains.user.dto.UserDto.SelectUserRes;
import com.j2kb.codev21.domains.user.dto.UserDto.UserIdRes;
import com.j2kb.codev21.domains.user.dto.mapper.UserMapper;
import com.j2kb.codev21.domains.user.service.UserService;
import com.j2kb.codev21.global.common.CommonResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    //회원 전체 조회
    //@PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @GetMapping("/admin/users")
    public CommonResponse<List<SelectUserRes>> selectAllUser() {
        return CommonResponse.<List<SelectUserRes>>builder()
            .code("200")
            .message("ok")
            .data(userService.getUserList()).build();
    }

    //회원가입
    @PostMapping("/users")
    public CommonResponse<UserIdRes> joinUser(
        @RequestBody @Valid UserDto.JoinReq dto) {
        return CommonResponse.<UserIdRes>builder()
            .code("200")
            .message("ok")
            .data(userService.joinUser(userMapper.joinDtoToEntity(dto))).build();
    }

    //회원 단건 조회
    @GetMapping(value = {"/users/{userId}", "/admin/users/{userId}"})
    public CommonResponse<SelectUserRes> selectUser(
        @PathVariable("userId") Long userId) {
        return CommonResponse.<SelectUserRes>builder()
            .code("200")
            .message("ok")
            .data(userService.getUser(userId)).build();
    }

    //회원수정(유저 권한)
    @PatchMapping(value = "/users/{userId}")
    //로그인 구현하고 테스트 ★★★★★★★★★★★★★★★★★
    //@PreAuthorize("isAuthenticated() and (( #dto.getEmail() == principal.username ) and hasRole('ROLE_USER'))")
    //@PreAuthorize("(( #dto.email == principal.username ) and hasRole('ROLE_USER'))")
    public CommonResponse<SelectUserRes> updateUser(
        @PathVariable("userId") Long userId,
        @RequestBody @Valid UserDto.UpdateUserReq dto) {
        return CommonResponse.<SelectUserRes>builder()
            .code("200")
            .message("ok")
            .data(userService.updateUser(userId, userMapper.updateUserDtoToEntity(dto))).build();
    }

    //회원수정(관리자 권한)
    @PatchMapping(value = "/admin/users/{userId}")
    //로그인 구현하고 테스트 ★★★★★★★★★★★★★★★★★
    //@PreAuthorize("isAuthenticated() and (( #dto.getEmail() == principal.username ) and hasRole('ROLE_ADMIN'))")
    public CommonResponse<SelectUserRes> updateUserByAdmin(
        @PathVariable("userId") final Long userId,
        @RequestBody @Valid UserDto.UpdateUserByAdminReq dto) {
        return CommonResponse.<SelectUserRes>builder()
            .code("200")
            .message("ok")
            .data(userService.updateUserByAdmin(userId, userMapper.updateUserByAdminDtoToEntity(dto))).build();
    }

    //회원삭제
    @DeleteMapping(value = {"/users/{userId}", "/admin/users/{userId}"})
    //로그인 구현하고 테스트 ★★★★★★★★★★★★★★★★★
    //@PreAuthorize("isAuthenticated() and (( #dto.getEmail() == principal.username ))")
    public CommonResponse<UserDto.DeleteUserCheckRes> deleteUser(
        @PathVariable("userId") final Long userId) {
        return CommonResponse.<UserDto.DeleteUserCheckRes>builder()
            .code("200")
            .message("ok")
            .data(userService.deleteUser(userId))
            .build();
    }

}
