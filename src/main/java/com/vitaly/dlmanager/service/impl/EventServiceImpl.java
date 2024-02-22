package com.vitaly.dlmanager.service.impl;
//  22-Feb-24
// gh crazym8nd


import com.vitaly.dlmanager.entity.event.EventEntity;
import com.vitaly.dlmanager.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class EventServiceImpl implements EventService {
    @Override
    public EventEntity create(EventEntity event) {
        return null;
    }

    @Override
    public EventEntity update(EventEntity event) {
        return null;
    }

    @Override
    public EventEntity getById(Long aLong) {
        return null;
    }

    @Override
    public List<EventEntity> getAll() {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }
}
