package com.vitaly.dlmanager.dto;
//  22-Feb-24
// gh crazym8nd


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vitaly.dlmanager.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileDto {
    private Long id;

    private String fileName;
    private String location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;
    private Long userId;

}
