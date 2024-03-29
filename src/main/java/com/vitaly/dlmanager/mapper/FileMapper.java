package com.vitaly.dlmanager.mapper;


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
