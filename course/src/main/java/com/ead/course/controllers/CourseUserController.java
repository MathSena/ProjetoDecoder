package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CourseUserController {

  @Autowired CourseClient courseClient;

  @GetMapping("/courses/{courseId}/users")
  public ResponseEntity<Page<UserDto>> getAllUsersByCourse(
      @PageableDefault(sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
      @PathVariable(value = "courseId") UUID courseId) {

    return ResponseEntity.status(HttpStatus.OK)
        .body(courseClient.getAllUsersByCouse(courseId, pageable));
  }
}
