package com.vitaly.dlmanager.service;
//  22-Feb-24
// gh crazym8nd


import java.util.List;

public interface GenericService <T,ID>{
    T getById(ID id);
    List<T> getAll();

    void deleteById(ID id);
}
