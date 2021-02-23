package com.j2kb.codev21.domains.user.dto.mapper;

import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
        @Mapping(target = "status", constant = "ACTIVE"),
        @Mapping(target = "field", constant = "NONE")
    })
    User userDtoToEntity(UserDto.JoinReq dto);

//    @Mapping(target = "img", expression = "java(order.getProduct() + \".jpg\")") // 4
//    UserDto.joinReq userToJoinReq(User order);

}
