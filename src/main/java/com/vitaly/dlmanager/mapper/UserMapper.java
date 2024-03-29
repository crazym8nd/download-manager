package com.vitaly.dlmanager.mapper;



import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.user.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {FileMapper.class, EventMapper.class})
public interface UserMapper {
    UserDto map(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity map(UserDto dto);
}
