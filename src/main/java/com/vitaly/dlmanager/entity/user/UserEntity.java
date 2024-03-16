package com.vitaly.dlmanager.entity.user;
//  17-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.event.EventEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity implements Persistable<Long> {

    @Id
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;

    @Transient
    private List<EventEntity> events;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}
