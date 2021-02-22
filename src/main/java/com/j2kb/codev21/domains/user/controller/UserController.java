package com.j2kb.codev21.domains.user.controller;

import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserOnlyIdRes;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserRes;
import com.j2kb.codev21.domains.user.service.UserService;
import com.j2kb.codev21.global.common.CommonResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    //회원 전체 조회

    //@PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN'))" )
    @GetMapping("/admin/users")
    public CommonResponse<List<selectUserRes>> selectAllUser() {
        return CommonResponse.<List<selectUserRes>>builder()
            .code("200")
            .message("ok")
            .data(userService.getUserList()).build();
    }

    //회원가입
    @PostMapping("/users")
    public CommonResponse<selectUserOnlyIdRes> joinUser(
        @RequestBody @Valid UserDto.joinReq dto) {

        return CommonResponse.<selectUserOnlyIdRes>builder()
            .code("200")
            .message("ok")
            .data(userService.joinUser(dto)).build();
    }

    //회원 단건 조회
    @GetMapping(value = {"/users/{userId}", "/admin/users/{userId}"})
    public CommonResponse<selectUserRes> selectUser(
        @PathVariable("userId") Long userId) {

        return CommonResponse.<selectUserRes>builder()
            .code("200")
            .message("ok")
            .data(userService.getUser(userId)).build();
    }

    //회원수정(유저 권한)
    @PatchMapping(value = "/users/{userId}")
    public CommonResponse<selectUserRes> updateUser(
        @PathVariable("userId") Long userId,
        @RequestBody @Valid UserDto.updateUserReq dto) {

        return CommonResponse.<selectUserRes>builder()
            .code("200")
            .message("ok")
            .data(userService.updateUser(userId, dto)).build();
    }

    //회원수정(관리자 권한)
    //@PreAuthorize("isAuthenticated() and ( hasRole('ROLE_ADMIN'))" )
    @PatchMapping(value = "/admin/users/{userId}")
    public CommonResponse<selectUserRes> updateUserByAdmin(
        @PathVariable("userId") final Long userId,
        @RequestBody @Valid UserDto.updateUserByAdminReq dto) {

        return CommonResponse.<selectUserRes>builder()
            .code("200")
            .message("ok")
            .data(userService.updateUserByAdmin(userId, dto)).build();
    }

    //회원삭제
    @DeleteMapping(value = {"/users/{userId}", "/admin/users/{userId}"})
    public CommonResponse<Map> deleteUser(
        @PathVariable("userId") final Long userId) {

        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("result", userService.deleteUser(userId));

        return CommonResponse.<Map>builder()
            .code("200")
            .message("ok")
            .data(map)
            .build();
    }

}
