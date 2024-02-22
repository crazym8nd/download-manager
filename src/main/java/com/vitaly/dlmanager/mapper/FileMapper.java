package com.vitaly.dlmanager.mapper;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.dto.FileDto;
import com.vitaly.dlmanager.entity.file.FileEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileDto map(FileEntity fileEntity);

    @InheritInverseConfiguration
    FileEntity map(FileDto dto);
}
