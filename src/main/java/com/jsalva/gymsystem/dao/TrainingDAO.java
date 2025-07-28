package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.model.Training;

import java.util.List;

public interface TrainingDAO {
    void save(Training training);
    List<Training> findAll();
    Training findById(Long id);
}
