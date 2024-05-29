package com.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mapper.StudentMapper;
import com.po.Student;
import com.service.StudentService;

@Service
@Transactional
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentMapper studentMapper;

    public List<Student> findAllStudent(){
        return studentMapper.selectStudents();
    }
    public Student findStudentBySno(String sno){
        return studentMapper.selectStudentBySno(sno);
    }
    public void addStudent(Student student){
        studentMapper.insertStudent(student);
    }
    public void deleteStudents(String[] snoArray){
        for (int i = 0; i < snoArray.length; i ++){
            studentMapper.deleteStudentBySno(snoArray[i]);
        }
    }
    public void updateStudent(Student student){
        studentMapper.updateStudent(student);
    }
}
