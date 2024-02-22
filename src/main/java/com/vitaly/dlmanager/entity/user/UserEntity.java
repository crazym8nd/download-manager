package com.vitaly.dlmanager.entity.user;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.Status;
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
    private Status status;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }
}
