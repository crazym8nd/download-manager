package com.vitaly.dlmanager;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.UserEntity;
import com.vitaly.dlmanager.mapper.UserMapper;
import com.vitaly.dlmanager.mapper.UserMapperImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class test {
    @Autowired
    UserMapper userMapper;
    @Test
    public void test(){
        UserDto dto = new UserDto();
        dto.setUsername("test");
        dto.setPassword("password");

        UserEntity entity = userMapper.map(dto);


        assertEquals(dto.getUsername(), entity.getUsername());
        assertEquals(dto.getPassword(), entity.getPassword());
    }
}