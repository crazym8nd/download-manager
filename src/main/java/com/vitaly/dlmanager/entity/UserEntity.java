package com.vitaly.dlmanager.entity;
//  17-Feb-24
// gh crazym8nd


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;



@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity {

    @Id
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }
}
