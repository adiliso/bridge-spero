package com.adil.bridgespero.security.mapper;

import com.adil.bridgespero.domain.entity.UserEntity;
import com.adil.bridgespero.domain.model.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper2 extends EntityMapper<UserDto, UserEntity> {

    UserMapper2 INSTANCE = Mappers.getMapper(UserMapper2.class);

}
