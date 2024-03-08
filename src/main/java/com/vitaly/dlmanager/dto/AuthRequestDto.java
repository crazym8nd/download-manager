package com.vitaly.dlmanager.dto;
//  17-Feb-24
// gh crazym8nd

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRequestDto {

    private String username;
    private String password;
}
