package com.vitaly.dlmanager.util;

import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.entity.user.UserRole;

import java.time.LocalDateTime;

public class UserDataUtils {
        //Entities
    public static UserEntity getFirstUserTransient(){
        return UserEntity.builder()
                .username("testname")
                .email("testmail@gmail.com")
                .password("test1")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserEntity getSecondUserTransient(){
        return UserEntity.builder()
                .username("Alex")
                .email("alex@gmail.com")
                .password("test1")
                .role(UserRole.MODERATOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserEntity getThirdUserTransient(){
        return UserEntity.builder()
                .username("Vitaly")
                .email("vitlaly@gmail.com")
                .password("testtest")
                .role(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserEntity getFirstUserPersisted(){
        return UserEntity.builder()
                .id(1L)
                .username("testname")
                .email("testmail@gmail.com")
                .password("test1")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserEntity getSecondUserPersisted(){
        return UserEntity.builder()
                .id(2L)
                .username("Alex")
                .email("alex@gmail.com")
                .password("test1")
                .role(UserRole.MODERATOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserEntity getThirdUserPersisted(){
        return UserEntity.builder()
                .id(3L)
                .username("Vitaly")
                .email("vitlaly@gmail.com")
                .password("testtest")
                .role(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    //Dtos

    public static UserDto getUserDtoForRegistration(){
        return UserDto.builder()
                .username("TestUser")
                .email("testmail@gmail.com")
                .password("test")
                .build();
    }

    public static UserDto getFirstUserDtoTransient(){
        return UserDto.builder()
                .username("testname")
                .email("testmail@gmail.com")
                .password("test1")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserDto getSecondUserDtoTransient(){
        return UserDto.builder()
                .username("Alex")
                .email("alex@gmail.com")
                .password("test1")
                .role(UserRole.MODERATOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserDto getThirdUserDtoTransient(){
        return UserDto.builder()
                .username("Vitaly")
                .email("vitlaly@gmail.com")
                .password("testtest")
                .role(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserDto getFirstUserDtoPersisted(){
        return UserDto.builder()
                .id(1L)
                .username("testname")
                .email("testmail@gmail.com")
                .password("test1")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserDto getSecondUserDtoPersisted(){
        return UserDto.builder()
                .id(2L)
                .username("Alex")
                .email("alex@gmail.com")
                .password("test1")
                .role(UserRole.MODERATOR)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserDto getThirdUserDtoPersisted(){
        return UserDto.builder()
                .id(3L)
                .username("Vitaly")
                .email("vitlaly@gmail.com")
                .password("testtest")
                .role(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }
}
