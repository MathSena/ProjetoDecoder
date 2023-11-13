package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

  String requestUri = "http://localhost:8087";

  public String createUrl(UUID courseId, Pageable pageable) {
    return requestUri
        + "/users?courseId="
        + courseId
        + "&page="
        + pageable.getPageNumber()
        + "&size="
        + pageable.getPageSize()
        + "&sort="
        + pageable.getSort().toString().replace(":", ",");
  }
}
