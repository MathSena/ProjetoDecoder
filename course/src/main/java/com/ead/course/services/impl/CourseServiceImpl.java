package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             ModuleRepository moduleRepository,
                             LessonRepository lessonRepository) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
    }

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        UUID courseId = courseModel.getCourseId();
        logger.info("Initiating deletion for course with ID: {}", courseId);

        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseId);

        for (ModuleModel moduleModel : moduleModelList) {
            UUID moduleId = moduleModel.getModuleId();
            List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleId);

            if (!lessonModelList.isEmpty()) {
                logger.debug("Found {} lessons related to module {}. Deleting them.", lessonModelList.size(), moduleId);
                lessonRepository.deleteAll(lessonModelList);
            }
        }

        if (!moduleModelList.isEmpty()) {
            logger.debug("Deleting {} modules related to course {}", moduleModelList.size(), courseId);
            moduleRepository.deleteAll(moduleModelList);
        }

        logger.debug("Deleting course with ID: {}", courseId);
        courseRepository.delete(courseModel);
        logger.info("Deletion completed for course with ID: {}", courseId);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        UUID courseId = courseModel.getCourseId();

        if (courseId == null) {
            logger.info("Initiating saving of a new course.");
        } else {
            logger.info("Initiating update for course with ID: {}", courseId);
        }

        CourseModel savedCourse = courseRepository.save(courseModel);
        logger.info("Course saved with ID: {}", savedCourse.getCourseId());
        return savedCourse;
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        logger.debug("Fetching course with ID: {}", courseId);
        return courseRepository.findById(courseId);
    }

    @Override
    public List<CourseModel> findAll() {
        logger.debug("Fetching all courses.");
        return courseRepository.findAll();
    }
}
