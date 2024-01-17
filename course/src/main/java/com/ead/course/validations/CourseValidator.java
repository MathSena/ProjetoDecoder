package com.ead.course.validations;

import com.ead.course.dtos.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

  @Autowired
  @Qualifier("defaultValidator")
  private Validator validator;

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

  }
}
