package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.dao.TrainerDAO;
import com.jsalva.gymsystem.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private TrainerDAO trainerDAO;


}
