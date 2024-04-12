package com.vitaly.dlmanager.util;

import com.vitaly.dlmanager.dto.FileDto;
import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.file.FileEntity;

import java.time.LocalDateTime;

public class FileDataUtils {
        //Entities
    public static FileEntity getFirstFileTransient(){
        return FileEntity.builder()
                .fileName("123.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/123.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileEntity getSecondFileTransient(){
        return FileEntity.builder()
                .fileName("222.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/222.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileEntity getThirdFileTransient(){
        return FileEntity.builder()
                .fileName("333.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/333.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }
    public static FileEntity getFileForUserEventSaveTestTransient(){
        return FileEntity.builder()
                .fileName("444.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/444.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }
    public static FileEntity getFileForModeratorEventSaveTestTransient(){
        return FileEntity.builder()
                .fileName("555.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/555.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(2L)
                .build();
    }
    public static FileEntity getFileForAdminEventSaveTestTransient(){
        return FileEntity.builder()
                .fileName("666.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/666.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(3L)
                .build();
    }
    public static FileEntity getFileForModEventDeleteTestTransient(){
        return FileEntity.builder()
                .fileName("777.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/777.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(2L)
                .build();
    }
    public static FileEntity getFileForDminEventDeleteTestTransient(){
        return FileEntity.builder()
                .fileName("888.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/888.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(3L)
                .build();
    }

    //Dto

    public static FileDto getFirstFileDtoTransient(){
        return FileDto.builder()
                .fileName("123.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/123.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileDto getSecondFileDtoTransient(){
        return FileDto.builder()
                .fileName("222.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/222.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileDto getThirdFileDtoTransient(){
        return FileDto.builder()
                .fileName("333.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/333.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

}
