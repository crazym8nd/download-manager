package com.vitaly.dlmanager.mapper;
    


import com.vitaly.dlmanager.dto.EventDto;
import com.vitaly.dlmanager.entity.event.EventEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDto map(EventEntity eventEntity);

    @InheritInverseConfiguration
    EventEntity map(EventDto dto);
}
