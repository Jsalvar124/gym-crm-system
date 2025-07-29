package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.model.Trainer;

import java.util.List;
import java.util.Map;

public interface TrainerDAO {
    void save(Trainer trainer);
    List<Trainer> findAll();
    Trainer findById(Long id);
    void update(Trainer trainer);
    void delete(Long id);
}
