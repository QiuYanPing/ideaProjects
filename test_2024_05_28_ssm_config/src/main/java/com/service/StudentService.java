package com.service;
import java.util.List;
import com.po.Student;

public interface StudentService {
    List<Student> findAllStudent();
    Student findStudentBySno(String sno);
    void addStudent(Student student);
    void deleteStudents(String[] snoArray);
    void updateStudent(Student student);
}
