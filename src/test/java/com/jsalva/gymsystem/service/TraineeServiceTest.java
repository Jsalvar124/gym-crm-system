//package com.jsalva.gymsystem.service;
//
//import com.jsalva.gymsystem.config.AppConfig;
//import com.jsalva.gymsystem.model.Trainee;
//import com.jsalva.gymsystem.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//
//import java.time.LocalDate;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringJUnitConfig(AppConfig.class)
//@DisplayName("Trainee Service Tests")
//public class TraineeServiceTest {
//
//    @Autowired
//    private Map<String, Map<Long, Object>> commonStorage;
//
//    @Autowired
//    private TraineeService traineeService;
//
//    private Map<Long, Object> trainees;
//
//    @BeforeEach
//    void setUp() {
//        trainees = commonStorage.get("trainees");
//        trainees.clear(); // Clean test state
//        User.setIdCount(1L); // Reset ID counter
//    }
//
//    @Test
//    @DisplayName("Trainee creation test")
//    void testTraineeCreation() {
//        traineeService.createTrainee("Ana", "Gómez", "CL 52 # 14-20", LocalDate.of(1991, 12, 13));
//        assertEquals(1, trainees.size());
//
//        Trainee t1 = (Trainee) trainees.get(1L);
//        assertEquals("Ana", t1.getFirstName());
//        assertEquals("Gómez", t1.getLastName());
//        assertEquals("Ana.Gómez", t1.getUsername());
//        assertEquals("CL 52 # 14-20", t1.getAddress());
//        assertEquals(LocalDate.of(1991, 12, 13), t1.getDateOfBirth());
//    }
//
//    @Test
//    @DisplayName("Username homonyms handling")
//    void testUsernameHomonyms() {
//        traineeService.createTrainee("Ana", "Gómez", "CL 52 # 14-20", LocalDate.of(1990, 1, 1));
//        traineeService.createTrainee("Ana", "Gómez", "CL 53 # 14-20", LocalDate.of(1992, 2, 2));
//        traineeService.createTrainee("Ana", "Gómez", "CL 54 # 14-20", LocalDate.of(1993, 3, 3));
//
//        assertEquals("Ana.Gómez", ((Trainee) trainees.get(1L)).getUsername());
//        assertEquals("Ana.Gómez1", ((Trainee) trainees.get(2L)).getUsername());
//        assertEquals("Ana.Gómez2", ((Trainee) trainees.get(3L)).getUsername());
//    }
//
//    @Test
//    @DisplayName("Password generation format")
//    void testPasswordFormat() {
//        traineeService.createTrainee("Ana", "Gómez", "CL 52 # 14-20", LocalDate.of(1990, 1, 1));
//        traineeService.createTrainee("Ana", "Gómez", "CL 52 # 14-20", LocalDate.of(1990, 1, 1));
//
//        Trainee t = (Trainee) trainees.get(1L);
//        Trainee t1 = (Trainee) trainees.get(2L);
//
//        assertNotEquals(t.getPassword(), t1.getPassword());
//        assertEquals(10, t.getPassword().length());
//        assertTrue(t.getPassword().matches("[a-zA-Z0-9]{10}"));
//    }
//
//    @Test
//    @DisplayName("Trainee update")
//    void testTraineeUpdate() {
//        traineeService.createTrainee("Ana", "Gómez", "123 St", LocalDate.of(1990, 1, 1));
//        traineeService.updateTrainee(1L, "Elena", null, "newPass123", false, "New Address", LocalDate.of(1988, 8, 8));
//
//        Trainee updated = (Trainee) trainees.get(1L);
//        assertEquals("Elena", updated.getFirstName());
//        assertEquals("Gómez", updated.getLastName());
//        assertEquals("Elena.Gómez", updated.getUsername());
//        assertEquals("newPass123", updated.getPassword());
//        assertEquals("New Address", updated.getAddress());
//        assertEquals(LocalDate.of(1988, 8, 8), updated.getDateOfBirth());
//        assertFalse(updated.isActive());
//    }
//
//    @Test
//    @DisplayName("Update with invalid ID")
//    void testUpdateInvalidId() {
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.updateTrainee(999L, "Name", "Last", null, null, null, null);
//        });
//        assertEquals("Trainee with Id 999 not found.", ex.getMessage());
//    }
//
//    @Test
//    @DisplayName("Trainee deletion")
//    void testDeleteTrainee() {
//        traineeService.createTrainee("Ana", "Gómez", "123 St", LocalDate.of(1990, 1, 1));
//        assertEquals(1, trainees.size());
//
//        traineeService.deleteTrainee(1L);
//        assertEquals(0, trainees.size());
//    }
//
//    @Test
//    @DisplayName("Deletion with invalid ID")
//    void testDeleteInvalidId() {
//        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.deleteTrainee(999L);
//        });
//        assertEquals("Trainee with Id 999 not found.", ex.getMessage());
//    }
//}
