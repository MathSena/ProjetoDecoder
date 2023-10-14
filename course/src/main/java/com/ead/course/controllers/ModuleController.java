package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

  @Autowired ModuleService moduleService;

  @Autowired CourseService courseService;

  private static final String MODULE_NOT_FOUND_MSG = "Module with ID {} not found.";

  /**
   * Saves a module for a course.
   *
   * @param courseId The ID of the course.
   * @param moduleDto The details of the module to be saved.
   * @return A ResponseEntity with details of the result.
   */
  @PostMapping("/courses/{courseId}/modules")
  public ResponseEntity<Object> saveModule(
      @PathVariable UUID courseId, @RequestBody @Valid ModuleDto moduleDto) {

    log.info("Attempting to save a module for course with ID: {}", courseId);

    Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
    if (courseModelOptional.isEmpty()) {
      log.warn(MODULE_NOT_FOUND_MSG, courseId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MODULE_NOT_FOUND_MSG);
    }

    ModuleModel moduleModel = new ModuleModel();
    BeanUtils.copyProperties(moduleDto, moduleModel);
    moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
    moduleModel.setCourse(courseModelOptional.get());
    log.info("Saving a new module.");
    return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
  }

  /**
   * Deletes a module from a course.
   *
   * @param courseId The ID of the course.
   * @param moduleId The ID of the module to be deleted.
   * @return A ResponseEntity with details of the result.
   */
  @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
  public ResponseEntity<Object> deleteModule(
      @PathVariable UUID courseId, @PathVariable UUID moduleId) {

    log.info("Attempting to delete module with ID: {} from course with ID: {}", moduleId, courseId);

    Optional<ModuleModel> moduleModelOptional =
        moduleService.findModuleIntoCourse(courseId, moduleId);
    if (moduleModelOptional.isEmpty()) {
      log.warn(MODULE_NOT_FOUND_MSG, moduleId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MODULE_NOT_FOUND_MSG);
    }

    moduleService.delete(moduleModelOptional.get());
    log.info("Module with ID: {} successfully deleted from course ID: {}", moduleId, courseId);
    return ResponseEntity.status(HttpStatus.OK).body("Module deleted");
  }

  /**
   * Retrieves all modules associated with a course.
   *
   * @param courseId The ID of the course.
   * @param pageable Pagination details.
   * @return A list of modules for the course.
   */
  @GetMapping("/courses/{courseId}/modules")
  public ResponseEntity<Page<ModuleModel>> getAllModules(
      @PathVariable(value = "courseId") UUID courseId,
      SpecificationTemplate.ModuleSpec spec,
      @PageableDefault(page = 0, size = 10, sort = "moduleId", direction = Sort.Direction.ASC)
          Pageable pageable) {
    log.info("Fetching all modules for course with ID: {}", courseId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            moduleService.findAllByCourse(
                SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
  }

  /**
   * Retrieves details of a specific module associated to a course.
   *
   * @param courseId The ID of the course.
   * @param moduleId The ID of the module to be fetched.
   * @return Details of the module or an error message.
   */
  @GetMapping("/courses/{courseId}/modules/{moduleId}")
  public ResponseEntity<Object> getOneModule(
      @PathVariable UUID courseId, @PathVariable UUID moduleId) {
    log.info("Fetching details for module with ID: {} for course with ID: {}", moduleId, courseId);
    Optional<ModuleModel> moduleModelOptional =
        moduleService.findModuleIntoCourse(courseId, moduleId);
    if (moduleModelOptional.isEmpty()) {
      log.warn(MODULE_NOT_FOUND_MSG, moduleId);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
    }
    return ResponseEntity.status(HttpStatus.OK).body(moduleModelOptional.get());
  }
}
