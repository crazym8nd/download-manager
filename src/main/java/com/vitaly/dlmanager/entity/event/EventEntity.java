package com.vitaly.dlmanager.entity.event;
    


import com.vitaly.dlmanager.entity.Status;
import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.entity.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("events")
public class EventEntity implements Persistable<Long> {
    @Id
    private Long id;
    private Long userId;
    private Long fileId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;

    @Transient
    private UserEntity user;

    @Transient
    private FileEntity file;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}
