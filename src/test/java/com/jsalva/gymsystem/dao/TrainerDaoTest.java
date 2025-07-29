package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.dao.impl.TrainerDAOImpl;
import com.jsalva.gymsystem.model.Trainer;
import com.jsalva.gymsystem.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TrainerDaoTest {

    private TrainerDAOImpl trainerDAO;
    private Map<String, Map<Long, Object>> commonStorage;

    private Trainer trainer1;
    private Trainer trainer2;

    @BeforeEach
    void setUp() {
        User.setIdCount(1L);
        trainerDAO = new TrainerDAOImpl();
        commonStorage = new HashMap<>();
        Map<Long, Object> trainerMap = new HashMap<>();
        commonStorage.put("trainers", trainerMap);

        trainerDAO.setCommonStorage(commonStorage);

        trainer1 = new Trainer();
        trainer1.setFirstName("José");

        trainer2 = new Trainer();
        trainer2.setFirstName("Ana");
    }

    @Test
    void testSaveAndFindById() {
        trainerDAO.save(trainer1);
        Trainer found = trainerDAO.findById(1L);
        System.out.println(found);
        assertNotNull(found);
        assertEquals("José", found.getFirstName());
    }

    @Test
    void testFindAll() {
        trainerDAO.save(trainer1);
        trainerDAO.save(trainer2);
        List<Trainer> all = trainerDAO.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testUpdate() {
        trainerDAO.save(trainer1);
        trainer1.setUsername("UpdatedUserName");
        trainerDAO.update(trainer1);
        Trainer updated = trainerDAO.findById(1L);
        assertEquals("UpdatedUserName", updated.getUsername());
    }

    @Test
    void testDelete() {
        trainerDAO.save(trainer1);
        trainerDAO.delete(1L);
        assertNull(trainerDAO.findById(1L));
    }

    @Test
    void testFindByIdWithNullIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> trainerDAO.findById(null));
    }

    @Test
    void testUpdateWithNullTrainerThrows() {
        assertThrows(IllegalArgumentException.class, () -> trainerDAO.update(null));
    }


    @Test
    void testDeleteWithNullIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> trainerDAO.delete(null));
    }
}
