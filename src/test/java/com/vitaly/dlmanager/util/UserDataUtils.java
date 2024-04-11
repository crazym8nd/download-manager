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
                .password("mvwAEso29ZSiJ2VckPpPvoJQ32Ht+CYCenecgiXUMjY=")
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
                .password("af17zMKqhPzkVaIkPj5W3cjrNI3+gTaqAkZ90WnQ06w=")
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
                .password("QvZ8ivl5zRLsJgFvNW6SvvC8qs3XnTh60FcaYInE4Wk=")
                .role(UserRole.ADMIN)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    //Dtos

    public static UserDto getUserDtoForSaving(){
        return UserDto.builder()
                .username("UserToSave")
                .email("testmailtosave@gmail.com")
                .password("testpassowrdforsave")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    //AuthRequestDto for auth test
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
    //AuthRequestDto for user
    public static AuthRequestDto getUserModeratorDtoForLogin() {
        return AuthRequestDto.builder()
                .username("Alex")
                .password("test1")
                .build();
    }

    public static UserDto getUserDtoUpdated() {
        return UserDto.builder()
                .id(3L)
                .username("UpdatedUser")
                .password("UpdatedPassword")
                .email("updated@mail.com")
                .build();
    }
}
