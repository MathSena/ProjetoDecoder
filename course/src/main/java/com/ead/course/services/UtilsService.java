package com.ead.course.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UtilsService {

  String createUrl(UUID courseId, Pageable pageable);
}
