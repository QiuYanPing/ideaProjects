package com.mapper;
import java.util.List;
import com.po.Student;

public interface StudentMapper {
    Student selectStudentBySno(String sno);
    List<Student> selectStudentBySname(String sname);
    List<Student> selectStudents();
    void insertStudent(Student student);
    void updateStudent(Student student);
    void deleteStudentBySno(String sno);
}
