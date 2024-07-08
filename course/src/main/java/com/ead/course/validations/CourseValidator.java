package com.ead.course.validations;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        CourseDto courseDto = (CourseDto) o;
        validator.validate(courseDto, errors);
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        Optional<UserModel> userModel = userService.findById(userInstructor);

        if (userModel.isEmpty()) {
            errors.rejectValue("userInstructor", "userInstructor.not.found", "User instructor not found.");
        }
        if (userModel.get()
                .getUserType()
                .equals("STUDENT")) {
            errors.rejectValue("userInstructor", "userInstructor.not.instructor", "User instructor is not an instructor.");
        }


    }
}
