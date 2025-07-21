package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.model.Trainer;

import java.util.List;

public interface ITrainerDAO {
    Trainer save(Trainer trainer);
    List<Trainer> findAll();
    Trainer findById(Long id);
    Trainer update(Trainer trainer);
    void delete(Long id);
}
