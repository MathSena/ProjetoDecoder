package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class LessonController {
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ModuleService moduleService;
    private static final String LESSON_NOT_FOUND_MSG = "Lesson with ID {} not found for module: {}";

    /**
     * Saves a new lesson associated with a module.
     *
     * @param moduleId  The ID of the associated module.
     * @param lessonDto Data of the lesson to be saved.
     * @return HTTP Response representing the outcome of the operation.
     */
    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable UUID moduleId, @RequestBody @Valid LessonDto lessonDto) {
        log.info("Saving lesson for module: {}", moduleId);
        Optional<ModuleModel> moduleModelOptional = moduleService.findById(moduleId);
        if (moduleModelOptional.isEmpty()) {
            log.warn("Module with ID {} not found", moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lessonModel);
        lessonModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lessonModel.setModule(moduleModelOptional.get());
        log.info("Lesson saved successfully for module: {}", moduleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lessonModel));
    }

    /**
     * Deletes a specific lesson associated with a module.
     *
     * @param moduleId The ID of the module.
     * @param lessonId The ID of the lesson to be deleted.
     * @return HTTP Response representing the outcome of the operation.
     */
    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        log.info("Deleting lesson with ID {} for module: {}", lessonId, moduleId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            log.warn(LESSON_NOT_FOUND_MSG, lessonId, moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found to delete");
        }
        lessonService.delete(lessonModelOptional.get());
        log.info("Lesson with ID {} deleted successfully for module: {}", lessonId, moduleId);
        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    /**
     * Updates a specific lesson associated with a module.
     *
     * @param moduleId  The ID of the module.
     * @param lessonId  The ID of the lesson to be updated.
     * @param lessonDto Updated data of the lesson.
     * @return HTTP Response representing the outcome of the operation.
     */
    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId, @RequestBody @Valid LessonDto lessonDto) {
        log.info("Updating lesson with ID {} for module: {}", lessonId, moduleId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModelOptional.isEmpty()) {
            log.warn(LESSON_NOT_FOUND_MSG, lessonId, moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        var lessonModel = lessonModelOptional.get();
        lessonModel.setTitle(lessonDto.getTitle());
        lessonModel.setDescription(lessonDto.getDescription());
        lessonModel.setVideoUrl(lessonDto.getVideoUrl());
        log.info("Lesson with ID {} updated successfully for module: {}", lessonId, moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.save(lessonModel));
    }

    /**
     * Retrieves all lessons associated with a specific module.
     *
     * @param moduleId The ID of the module.
     * @param pageable Pagination and sorting information.
     * @return List of lessons associated with the specified module.
     */
    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> getAllLessons(@PathVariable(value = "moduleId") UUID moduleId, SpecificationTemplate.LessonSpec spec, @PageableDefault(page = 0, size = 10, sort = "lessonId", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Fetching all lessons for module: {}", moduleId);
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    /**
     * Retrieves a specific lesson associated with a module.
     *
     * @param moduleId The ID of the module.
     * @param lessonId The ID of the desired lesson.
     * @return Details of the specified lesson or an error if not found.
     */
    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable UUID moduleId, @PathVariable UUID lessonId) {
        log.info("Fetching lesson with ID {} for module: {}", lessonId, moduleId);
        Optional<LessonModel> lessonModelOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if (lessonModelOptional.isPresent()) {
            log.info("Found lesson with ID {} for module: {}", lessonId, moduleId);
            return ResponseEntity.status(HttpStatus.OK).body(lessonModelOptional.get());
        } else {
            log.warn(LESSON_NOT_FOUND_MSG, lessonId, moduleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
    }
}
