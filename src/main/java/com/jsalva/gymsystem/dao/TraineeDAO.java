package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.model.Trainee;

import java.util.List;

public interface TraineeDAO {
    Trainee save(Trainee trainee);
    List<Trainee> findAll();
    Trainee findById(Long id);
    Trainee update(Trainee trainee);
    void delete(Long id);
}
