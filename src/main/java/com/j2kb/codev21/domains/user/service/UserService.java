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
    public Long joinUser(){
        return 1L;
    }

    //회원 단건 조회
    public UserDto.selectUserRes getUser(Long userId){
        return new UserDto.selectUserRes();
    }

    //회원수정(유저권한)
    public UserDto.selectUserRes updateUser(Long userId, UserDto.updateUserReq dto){
        return new UserDto.selectUserRes();
    }

    //회원수정(관리자 권한)
    public UserDto.selectUserRes updateUserByAdmin(Long userId, UserDto.updateUserByAdminReq dto){
        return new UserDto.selectUserRes();
    }

    //회원삭제
    public boolean deleteUser(Long userId){
        return true;
    }

}
