package com.service;

import com.po.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAllCourse();

    void addCourse(Course course);

    void deleteCourses(Integer[] cnoArray);

    void updateCourse(Course course);
}
