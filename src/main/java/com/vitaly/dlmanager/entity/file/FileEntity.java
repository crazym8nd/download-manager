package com.vitaly.dlmanager.entity.file;
    


import com.vitaly.dlmanager.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("files")
public class FileEntity implements Persistable<Long> {
    @Id
    private Long id;

    private String fileName;
    private String location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Status status;

    private Long userId;

    @Override
    public boolean isNew() {
        return Objects.isNull(id);
    }
}
