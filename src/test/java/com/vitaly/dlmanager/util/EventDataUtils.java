package com.vitaly.dlmanager.util;

import com.vitaly.dlmanager.dto.EventDto;
import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.event.EventEntity;

import java.time.LocalDateTime;

public class EventDataUtils {

    //EventEntities

    public static EventEntity getFirstEventTransient(){
        return EventEntity.builder()
                .userId(1L)
                .fileId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventEntity getSecondEventTransient(){
        return EventEntity.builder()
                .userId(2L)
                .fileId(2L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventEntity getThirdEventTransient(){
        return EventEntity.builder()
                .userId(3L)
                .fileId(3L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventEntity getFirstEventPersisted(){
        return EventEntity.builder()
                .id(1L)
                .userId(1L)
                .fileId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventEntity getSecondEventPersisted(){
        return EventEntity.builder()
                .id(2L)
                .userId(2L)
                .fileId(2L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventEntity getThirdEventPersisted(){
        return EventEntity.builder()
                .id(3L)
                .userId(3L)
                .fileId(3L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    //EventDtos
    public static EventDto getFirstEventDtoTransient(){
        return EventDto.builder()
                .userId(1L)
                .fileId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventDto getSecondEventDtoTransient(){
        return EventDto.builder()
                .userId(2L)
                .fileId(2L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventDto getThirdEventDtoTransient(){
        return EventDto.builder()
                .userId(3L)
                .fileId(3L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventDto getFirstEventDtoPersisted(){
        return EventDto.builder()
                .id(1L)
                .userId(1L)
                .fileId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventDto getSecondEventDtoPersisted(){
        return EventDto.builder()
                .id(2L)
                .userId(2L)
                .fileId(2L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventDto getThirdEventDtoPersisted(){
        return EventDto.builder()
                .id(3L)
                .userId(3L)
                .fileId(3L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

}

