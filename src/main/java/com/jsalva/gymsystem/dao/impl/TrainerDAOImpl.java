package com.jsalva.gymsystem.dao.impl;

import com.jsalva.gymsystem.dao.ITrainerDAO;
import com.jsalva.gymsystem.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;
import java.util.Map;

public class TrainerDAOImpl implements ITrainerDAO {
    private Map<String, Map<Long, Object>> storage;

    // Setter Injection
    @Autowired
    public void setStorage(@Qualifier("storage") Map<String, Map<Long, Object>> storage) {
        this.storage = storage;
    }

    private final String namespace = "trainers";

    @Override
    public Trainer save(Trainer trainer) {
        return null;
    }

    @Override
    public List<Trainer> findAll() {
        return List.of();
    }

    @Override
    public Trainer findById(Long id) {
        return null;
    }

    @Override
    public Trainer update(Trainer trainer) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
