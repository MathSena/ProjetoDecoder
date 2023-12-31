package com.ead.authuser.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UtilsService {
  String createUrl(UUID userId, Pageable pageable);
}
