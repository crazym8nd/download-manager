package com.vitaly.dlmanager.util;

import com.vitaly.dlmanager.dto.AuthRequestDto;
import com.vitaly.dlmanager.dto.UserDto;
import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.entity.user.UserRole;

import java.time.LocalDateTime;

public class UserDataUtils {
        //Entities
    public static UserEntity getFirstUserTransient(){
        return UserEntity.builder()
                .username("testuser")
                .email("testmail@gmail.com")
                .password("testpass")
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

    public static UserDto getFirstUserDtoTransient(){
        return UserDto.builder()
                .username("testuser")
                .email("testmail@gmail.com")
                .password("testpass")
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
                .username("testuser")
                .email("testmail@gmail.com")
                .password("mvwAEso29ZSiJ2VckPpPvoJQ32Ht+CYCenecgiXUMjY=")
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

    //AuthRequestDto
    public static AuthRequestDto getUserDtoForLogin(){
        return AuthRequestDto.builder()
                .username("testuser")
                .password("testpass")
                .build();
    }

    //UserDto to register
    public static UserDto getUserDtoForRegister(){
        return UserDto.builder()
                .username("RegisteredUser")
                .password("MyPassword")
                .email("registered@mail.com")
                .build();
    }

}
