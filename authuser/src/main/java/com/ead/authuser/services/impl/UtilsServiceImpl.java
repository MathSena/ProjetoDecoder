package com.ead.authuser.services.impl;

import com.ead.authuser.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {
    String requestUri = "http://localhost:8082";

    public String createUrl(UUID userId, Pageable pageable) {
        return requestUri + "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size="
                + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replace(":", ",");
    }

}
