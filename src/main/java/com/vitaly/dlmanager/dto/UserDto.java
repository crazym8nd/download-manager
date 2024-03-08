package com.vitaly.dlmanager.dto;
//  17-Feb-24
// gh crazym8nd


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.user.UserRole;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    private Long id;
    private String username;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;
}
