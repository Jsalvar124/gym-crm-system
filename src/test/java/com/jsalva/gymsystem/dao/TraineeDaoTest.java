package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.dao.impl.TraineeDAOImpl;
import com.jsalva.gymsystem.model.Trainee;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TraineeDaoTest {
    private TraineeDAOImpl traineeDao;
    private Map<String, Map<Long, Object>> commonStorage;

    private Trainee trainee1;
    private Trainee  trainee2;

    @BeforeEach
    void setup(){
        User.setIdCount(1L);
        traineeDao = new TraineeDAOImpl();
        commonStorage = new HashMap<>();
        Map<Long, Object> traineeMap = new HashMap<>();

        commonStorage.put("trainees", traineeMap);

        traineeDao.setCommonStorage(commonStorage);

        trainee1 = new Trainee();
        trainee1.setFirstName("José");

        trainee2 = new Trainee();
        trainee2.setFirstName("Ana");
    }
    @Test
    void testSaveAndFindById() {
        traineeDao.save(trainee1);
        Trainee found = traineeDao.findById(1L);
        System.out.println(found);
        assertNotNull(found);
        assertEquals("José", found.getFirstName());
    }

    @Test
    void testFindAll() {
        traineeDao.save(trainee1);
        traineeDao.save(trainee2);
        List<Trainee> all = traineeDao.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testUpdate() {
        traineeDao.save(trainee1);
        trainee1.setUsername("UpdatedUserName");
        traineeDao.update(trainee1);
        Trainee updated = traineeDao.findById(1L);
        assertEquals("UpdatedUserName", updated.getUsername());
    }

    @Test
    void testDelete() {
        traineeDao.save(trainee1);
        traineeDao.delete(1L);
        assertNull(traineeDao.findById(1L));
    }

    @Test
    void testFindByIdWithNullIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> traineeDao.findById(null));
    }

    @Test
    void testUpdateWithNullTrainerThrows() {
        assertThrows(IllegalArgumentException.class, () -> traineeDao.update(null));
    }


    @Test
    void testDeleteWithNullIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> traineeDao.delete(null));
    }
}
