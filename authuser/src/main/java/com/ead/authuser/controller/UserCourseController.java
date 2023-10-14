package com.ead.authuser.controller;

import com.ead.authuser.clients.UserClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.specifications.SpecificationTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class UserCourseController {

  @Autowired UserClient userClient;

  @GetMapping("/users/{userId}/courses")
  public ResponseEntity<Page<CourseDto>> getAllCoursesByUser(
      SpecificationTemplate.UserSpec spec,
      @PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
      @PathVariable(value = "userId") UUID userId) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(userClient.getAllCoursesByUser(userId, pageable));
  }
}
