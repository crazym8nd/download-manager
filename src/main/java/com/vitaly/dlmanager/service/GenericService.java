package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface GenericService <T,ID>{

    Flux<T> getAll();
    Mono<T> getById(ID id);
    Mono update(T t);
    Mono save(T t);
    Mono delete(ID id);
}
