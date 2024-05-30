package com.service.impl;

import com.mapper.CourseMapper;
import com.po.Course;
import com.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Override
    public List<Course> findAllCourse() {
        return courseMapper.selectCourses();
    }

    @Override
    public void addCourse(Course course) {
        courseMapper.insertCourse(course);
    }

    @Override
    public void deleteCourses(Integer[] cnoArray) {
        for (int i = 0; i < cnoArray.length; i++) {
            courseMapper.deleteCourseByCno(cnoArray[i]);
        }
    }

    @Override
    public void updateCourse(Course course) {
        courseMapper.updateCourse(course);
    }
}
