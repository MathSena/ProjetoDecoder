package com.ead.course.services.impl;


import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModuleServiceImpl implements ModuleService {

    private static final Logger logger = LoggerFactory.getLogger(ModuleServiceImpl.class);

    private final LessonRepository lessonRepository;

    private final ModuleRepository moduleRepository;

    public ModuleServiceImpl(LessonRepository lessonRepository, ModuleRepository moduleRepository) {
        this.lessonRepository = lessonRepository;
        this.moduleRepository = moduleRepository;
    }


    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        logger.info("Initiating deletion for module with ID: {}", moduleModel.getModuleId());

        List<LessonModel> lessonModelList = lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());

        if (!lessonModelList.isEmpty()) {
            logger.debug("Found {} lessons related to module {}. Deleting them.", lessonModelList.size(), moduleModel.getModuleId());
            lessonRepository.deleteAll(lessonModelList);
        } else {
            logger.debug("No lessons found related to module {}. Proceeding to delete module.", moduleModel.getModuleId());
        }

        moduleRepository.delete(moduleModel);
        logger.info("Deletion completed for module with ID: {}", moduleModel.getModuleId());
    }
}
