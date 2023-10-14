package com.ead.course.clients;

import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
public class CourseClient {

  @Autowired RestTemplate restTemplate;

  @Autowired UtilsService utilsService;

  public Page<UserDto> getAllUsersByCouse(UUID courseId, Pageable pageable) {
    List<UserDto> searchResults = null;

    String url = utilsService.createUrl(courseId, pageable);

    log.debug("Request URL {}: ", url);
    log.info("Request URL {}: ", url);
    try {
      ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType =
          new ParameterizedTypeReference<>() {};
      ResponseEntity<ResponsePageDto<UserDto>> result =
          restTemplate.exchange(url, HttpMethod.GET, null, responseType);
      searchResults = Objects.requireNonNull(result.getBody()).getContent();
      log.debug("Response Number of elements: {}", searchResults.size());

    } catch (HttpStatusCodeException e) {
      log.error("Error request/courses: ", e);
    }
    log.info("Ending request /users courseId: {}", courseId);
    assert searchResults != null;
    return new PageImpl<>(searchResults);
  }
}
