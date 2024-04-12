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

    public static EventDto getEventDtoForSavingByUser() {
        return EventDto.builder()
                .userId(1L)
                .fileId(4L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }
    public static EventDto getEventDtoForSavingByModerator() {
        return EventDto.builder()
                .userId(2L)
                .fileId(5L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }
    public static EventDto getEventDtoForSavingByAdmin() {
        return EventDto.builder()
                .userId(3L)
                .fileId(6L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }

    public static EventDto getEventForUpdate() {
        return EventDto.builder()
                .userId(1L)
                .fileId(4L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }
    public static EventEntity getEventForDeleteByAdmin() {
        return EventEntity.builder()
                .userId(3L)
                .fileId(8L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }
    public static EventEntity getEventForDeleteByModerator() {
        return EventEntity.builder()
                .userId(1L)
                .fileId(7L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(Status.ACTIVE)
                .build();
    }
}

