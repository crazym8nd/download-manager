package com.vitaly.dlmanager.dto;
//  22-Feb-24
// gh crazym8nd


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.entity.user.UserEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EventDto {
    private Long id;
    private UserEntity user;
    private FileEntity file;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;
}
