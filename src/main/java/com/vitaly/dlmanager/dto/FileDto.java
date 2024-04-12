package com.vitaly.dlmanager.dto;
    


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vitaly.dlmanager.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private Long id;

    private String fileName;
    @JsonIgnore
    private String location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonIgnore
    private Status status;
    @JsonIgnore
    private Long userId;

}
