package com.j2kb.codev21.domains.user.service;

import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.selectUserRes;
import com.j2kb.codev21.domains.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NamedStoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;

    //회원 전체 조회
    public List<selectUserRes> getUserList(){
        return new ArrayList<>();
    }

    //회원가입
    public UserDto.selectUserOnlyIdRes joinUser(){
        return UserDto.selectUserOnlyIdRes.builder()
                .id(1L)
                .build();
    }

    //회원 단건 조회
    public UserDto.selectUserRes getUser(Long userId){
        return UserDto.selectUserRes.builder().build();
    }

    //회원수정(유저권한)
    public UserDto.selectUserRes updateUser(Long userId, UserDto.updateUserReq dto){
        return UserDto.selectUserRes.builder().build();
    }

    //회원수정(관리자 권한)
    public UserDto.selectUserRes updateUserByAdmin(Long userId, UserDto.updateUserByAdminReq dto){
        return UserDto.selectUserRes.builder().build();
    }

    //회원삭제
    public boolean deleteUser(Long userId){
        return true;
    }

}
