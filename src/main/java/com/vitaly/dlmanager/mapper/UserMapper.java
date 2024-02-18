package com.vitaly.dlmanager.mapper;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.UserEntity;
import com.vitaly.dlmanager.dto.UserDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto mapEntityToDto(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity mapDtoToEntity(UserDto userDto);

}
