package com.vitaly.dlmanager;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.UserEntity;
import com.vitaly.dlmanager.mapper.UserMapper;
import com.vitaly.dlmanager.mapper.UserMapperImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class test {
    UserMapper userMapper = new UserMapperImpl();
    @Test
    public void test(){
        UserDto dto = new UserDto();
        dto.setUsername("test");
        dto.setPassword("password");

        UserEntity entity = userMapper.mapDtoToEntity(dto);


        assertEquals(dto.getUsername(), entity.getUsername());
        assertEquals(dto.getPassword(), entity.getPassword());
    }
}
// MAPPER FIX!!!!