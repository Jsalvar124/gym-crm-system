package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.model.Trainee;

import java.util.List;

public interface TraineeDAO {
    void save(Trainee trainee);
    List<Trainee> findAll();
    Trainee findById(Long id);
    void update(Trainee trainee);
    void delete(Long id);
}
