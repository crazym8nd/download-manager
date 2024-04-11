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
    public static FileEntity getFileForEventSaveTestTransient(){
        return FileEntity.builder()
                .fileName("444.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/444.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }


    public static FileEntity getFirstFilePersisted(){
        return FileEntity.builder()
                .id(1L)
                .fileName("123.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/123.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileEntity getSecondFilePersisted(){
        return FileEntity.builder()
                .id(2L)
                .fileName("222.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/222.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileEntity getThirdFilePersisted(){
        return FileEntity.builder()
                .id(3L)
                .fileName("333.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/333.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
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

    public static FileDto getFirstFileDtoPersisted(){
        return FileDto.builder()
                .id(1L)
                .fileName("123.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/123.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileDto getSecondFileDtoPersisted(){
        return FileDto.builder()
                .id(2L)
                .fileName("222.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/222.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }

    public static FileDto getThirdFileDtoPersisted(){
        return FileDto.builder()
                .id(3L)
                .fileName("333.jpg")
                .location("https://storage.yandexcloud.net/springfluxr2dbc/333.jpg")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .userId(1L)
                .build();
    }
}
