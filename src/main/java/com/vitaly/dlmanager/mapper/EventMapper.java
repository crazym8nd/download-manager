package com.vitaly.dlmanager.mapper;
//  22-Feb-24
// gh crazym8nd


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
