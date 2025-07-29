package com.jsalva.gymsystem.dao;

import com.jsalva.gymsystem.dao.impl.TrainingDAOImpl;
import com.jsalva.gymsystem.model.Training;
import com.jsalva.gymsystem.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDaoTest {
    private TrainingDAOImpl trainingDao;
    private Map<String, Map<Long, Object>> commonStorage;
    private Training training1;
    private Training training2;

    @BeforeEach
    void setup() {
        trainingDao = new TrainingDAOImpl();
        commonStorage = new HashMap<>();
        Map<Long, Object> trainingMap = new HashMap<>();

        commonStorage.put("trainings", trainingMap);
        trainingDao.setCommonStorage(commonStorage);

        training1 = new Training();
        training1.setTrainingId(1L);
        training1.setTrainerId(10L);
        training1.setTraineeId(100L);
        training1.setTrainingName("Boxing Basics");
        training1.setTrainingType(TrainingType.BOXING);
        training1.setTrainingDate(LocalDate.of(2025, 7, 29));
        training1.setDuration(45);

        training2 = new Training();
        training2.setTrainingId(2L);
        training2.setTrainerId(11L);
        training2.setTraineeId(101L);
        training2.setTrainingName("Pilates Advanced");
        training2.setTrainingType(TrainingType.PILATES);
        training2.setTrainingDate(LocalDate.of(2025, 8, 1));
        training2.setDuration(60);
    }

    @Test
    void testSaveAndFindById() {
        trainingDao.save(training1);
        Training found = trainingDao.findById(1L);

        assertNotNull(found);
        assertEquals("Boxing Basics", found.getTrainingName());
    }

    @Test
    void testFindAll() {
        trainingDao.save(training1);
        trainingDao.save(training2);

        List<Training> trainings = trainingDao.findAll();
        assertEquals(2, trainings.size());
    }

    @Test
    void testFindByIdWithNullThrows() {
        assertThrows(IllegalArgumentException.class, () -> trainingDao.findById(null));
    }

    @Test
    void testSaveOverridesIfSameId() {
        trainingDao.save(training1);

        training1.setTrainingName("Updated Name");
        trainingDao.save(training1);

        Training updated = trainingDao.findById(1L);
        assertEquals("Updated Name", updated.getTrainingName());
    }

    @Test
    void testSaveAndRetrieveCorrectFields() {
        trainingDao.save(training2);
        Training result = trainingDao.findById(2L);

        assertEquals(11L, result.getTrainerId());
        assertEquals(101L, result.getTraineeId());
        assertEquals("Pilates Advanced", result.getTrainingName());
        assertEquals(TrainingType.PILATES, result.getTrainingType());
        assertEquals(LocalDate.of(2025, 8, 1), result.getTrainingDate());
        assertEquals(60, result.getDuration());
    }
}
