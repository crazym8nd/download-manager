package com.vitaly.dlmanager.repository;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.event.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface EventRepository extends R2dbcRepository<EventEntity,Long> {

}
