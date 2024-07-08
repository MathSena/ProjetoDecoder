package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CourseRepository
    extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    //TODO: Review the query

    // @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM CourseModel c WHERE c.courseId = :courseId AND c.userId = :userId")
    boolean existsByCourseAndUser(UUID courseId, UUID userId);

   // @Query(value="INSERT INTO CourseModel c (c.courseId, c.userId) VALUES (:courseId, :userId)")
    void saveCourseUser(UUID courseId, UUID userId);

}
