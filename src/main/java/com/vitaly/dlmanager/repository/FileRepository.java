package com.vitaly.dlmanager.repository;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.file.FileEntity;
import com.vitaly.dlmanager.entity.user.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface FileRepository extends R2dbcRepository<FileEntity,Long> {
}
