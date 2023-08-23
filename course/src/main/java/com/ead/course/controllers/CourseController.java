package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;


/**
 * This controller provides CRUD operations for Course entities.
 */
@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class CourseController {
    @Autowired
    CourseService courseService;
    private static final String COURSE_NOT_FOUND_MSG = "Course with ID {} not found.";

    /**
     * Save a new course.
     *
     * @param courseDto The course data transfer object.
     * @return Response entity with course data.
     */
    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody @Valid CourseDto courseDto) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDto, courseModel);
        courseModel.setCreatedDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastModifiedDate(LocalDateTime.now(ZoneId.of("UTC")));
        log.info("Saving a new course.");
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    /**
     * Delete an existing course.
     *
     * @param courseId The ID of the course to delete.
     * @return Response entity with status and message.
     */
    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn(COURSE_NOT_FOUND_MSG, courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(COURSE_NOT_FOUND_MSG);
        }
        courseService.delete(courseModelOptional.get());
        log.info("Course with ID {} deleted.", courseId);
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted");
    }

    /**
     * Update an existing course.
     *
     * @param courseId  The ID of the course to update.
     * @param courseDto The updated course data.
     * @return Response entity with updated course data.
     */
    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid CourseDto courseDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn(COURSE_NOT_FOUND_MSG, courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(COURSE_NOT_FOUND_MSG);
        }
        var courseModel = courseModelOptional.get();
        courseModel.setName(courseDto.getName());
        courseModel.setDescription(courseDto.getDescription());
        courseModel.setImageUrl(courseDto.getImageUrl());
        courseModel.setCourseStatus(courseDto.getCourseStatus());
        courseModel.setCourseLevel(courseDto.getCourseLevel());
        courseModel.setLastModifiedDate(LocalDateTime.now(ZoneId.of("UTC")));
        log.info("Updating course with ID {}.", courseId);
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }

    /**
     * Retrieve all courses.
     *
     * @return Response entity with a list of all courses.
     */
    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec, @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Fetching all courses.");
        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));
    }

    /**
     * Retrieve details of a single course.
     *
     * @param courseId The ID of the course to retrieve.
     * @return Response entity with course data.
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            log.warn(COURSE_NOT_FOUND_MSG, courseId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(COURSE_NOT_FOUND_MSG);
        }
        log.info("Fetching course with ID {}.", courseId);
        return ResponseEntity.status(HttpStatus.OK).body(courseModelOptional.get());
    }
}
