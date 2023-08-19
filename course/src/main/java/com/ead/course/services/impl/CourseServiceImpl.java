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
        logger.info("Initiating deletion for course with ID: {}", courseModel.getCourseId());

        List<ModuleModel> moduleModelList = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());

        if (!moduleModelList.isEmpty()) {
            logger.debug("Found {} modules related to course.", moduleModelList.size());

            for (ModuleModel moduleModel : moduleModelList) {
                List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

                if (!lessonModelList.isEmpty()) {
                    logger.debug("Found {} lessons related to module {}. Deleting them.", lessonModelList.size(), moduleModel.getModuleId());
                    lessonRepository.deleteAll(lessonModelList);
                }
            }

            logger.debug("Deleting all modules related to course {}", courseModel.getCourseId());
            moduleRepository.deleteAll(moduleModelList);
        }

        logger.debug("Deleting course with ID: {}", courseModel.getCourseId());
        courseRepository.delete(courseModel);
        logger.info("Deletion completed for course with ID: {}", courseModel.getCourseId());
    }
}
