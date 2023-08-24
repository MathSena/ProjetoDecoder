package com.ead.authuser.repository;

import com.ead.authuser.models.UserCourseModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {
}
