package com.jsalva.gymsystem.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T,K> {
    T create(T t);
    T update(T t);
    boolean delete(K id);
    Optional<T> findById(K id);
    List<T> findAll();
}
