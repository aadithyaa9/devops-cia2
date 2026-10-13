package com.university;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS301", "Data Structures", 4, 30, true, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS301", "Data Structures", 4, 30, false, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS401", "Advanced Algorithms", 4, 30, true, "CS301");
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {
        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");
        Course course = new Course("CS401", "Advanced Algorithms", 4, 30, true, "CS301");
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS301", "Data Structures", 4, 30, true, null);
        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCreditLimitIsExceeded() {
        Student student = new Student("S101", "Anita");
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);
        Course course = new Course("CS301", "Data Structures", 4, 30, true, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRegisterWhenOneSeatRemaining() {
        Student student1 = new Student("S101", "Anita");
        Student student2 = new Student("S102", "Rahul");
        Course course = new Course("CS501", "Capacity Test", 3, 2, true, null); 
        RegistrationService service = new RegistrationService();

        service.register(student1, course);
        
        boolean result = service.register(student2, course);

        assertTrue(result, "Registration should succeed for the last available seat");
        assertEquals(2, course.getCurrentEnrollment(), "Enrollment should equal maximum capacity");
        assertEquals(3, student2.getTotalCredits(), "Student should receive the course credits");
    }

    @Test
    void shouldRejectWhenCourseAtFullCapacity() {
        Student student1 = new Student("S101", "Anita");
        Student student2 = new Student("S102", "Rahul");
        Course course = new Course("CS502", "Full Course", 3, 1, true, null); 
        RegistrationService service = new RegistrationService();

        service.register(student1, course);
        boolean result = service.register(student2, course);

        assertFalse(result, "Registration should be rejected for a full course");
        assertEquals(1, course.getCurrentEnrollment(), "Enrollment must not exceed capacity limit");
        assertFalse(student2.isRegistered("CS502"), "Student state should not mutate on failure");
        assertEquals(0, student2.getTotalCredits(), "Student credits should remain unchanged");
    }


    @Test
    void shouldAccept1CreditCourse_BoundaryTest() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS101", "1 Credit Course", 1, 30, true, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);
        assertTrue(result, "Should accept a 1-credit course");
        assertEquals(1, student.getTotalCredits());
    }

    @Test
    void shouldAccept6CreditCourse_BoundaryTest() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS106", "6 Credit Course", 6, 30, true, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);
        assertTrue(result, "Should accept a 6-credit course");
        assertEquals(6, student.getTotalCredits());
    }

    @Test
    void shouldReject0CreditCourse_InvalidBoundary() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS000", "0 Credit Course", 0, 30, true, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);
        assertFalse(result, "Should reject a 0-credit course");
        assertEquals(0, student.getTotalCredits(), "Student credits should remain unchanged");
    }

    @Test
    void shouldReject7CreditCourse_InvalidBoundary() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CS007", "7 Credit Course", 7, 30, true, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);
        assertFalse(result, "Should reject a 7-credit course");
        assertEquals(0, student.getTotalCredits(), "Student credits should remain unchanged");
    }

    @Test
    void shouldRejectNegativeCreditCourse() {
        Student student = new Student("S101", "Anita");
        Course course = new Course("CSNEG", "Negative Credit", -2, 30, true, null);
        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);
        assertFalse(result, "Should reject negative credits");
        assertEquals(0, student.getTotalCredits(), "Student credits should remain unchanged");
    }
}