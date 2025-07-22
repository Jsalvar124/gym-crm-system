package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.model.Trainer;

import java.util.List;
import java.util.Map;

public interface TrainerDAO {
    Trainer save(Trainer trainer);
    List<Trainer> findAll();
    Trainer findById(Long id);
    Trainer update(Trainer trainer);
    void delete(Long id);
    Map<Long, Object> getTrainers(); // TODO REMOVE THIS IS JUST FOR MAIN TEST
}
