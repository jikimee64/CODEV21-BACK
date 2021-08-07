package com.j2kb.codev21.domains.user.dto.mapper;

import com.j2kb.codev21.domains.user.domain.User;
import com.j2kb.codev21.domains.user.dto.UserDto;
import com.j2kb.codev21.domains.user.dto.UserDto.SelectUserRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;

@Mapper(componentModel = "spring")
public interface UserMapper{

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
        @Mapping(target = "status", constant = "ACTIVE"),
        @Mapping(target = "field", constant = "NONE")
    })
    User joinDtoToEntity(UserDto.JoinReq dto);

    User updateUserDtoToEntity(UserDto.UpdateUserReq dto);

    User updateUserByAdminDtoToEntity(UserDto.UpdateUserByAdminReq dto);

    SelectUserRes userToDto(User user);

}
