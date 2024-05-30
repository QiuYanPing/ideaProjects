package com.mapper;

import com.po.Course;

import java.util.List;

public interface CourseMapper {
    List<Course> selectCourses();

    void insertCourse(Course course);

    void deleteCourseByCno(Integer s);

    void updateCourse(Course course);
}
