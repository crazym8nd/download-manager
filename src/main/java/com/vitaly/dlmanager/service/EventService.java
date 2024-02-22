package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.event.EventEntity;
import com.vitaly.dlmanager.entity.user.UserEntity;
import org.mapstruct.control.MappingControl;

public interface EventService extends GenericService<EventEntity,Long> {
    EventEntity create(EventEntity event);

    EventEntity update(EventEntity event);

}
