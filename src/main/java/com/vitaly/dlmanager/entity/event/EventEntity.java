package com.vitaly.dlmanager.entity.event;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.entity.user.UserEntity;
import com.vitaly.dlmanager.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("events")
public class EventEntity {
    @Id
    private Long id;
    private UserEntity user;
    private FileEntity file;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;

}
