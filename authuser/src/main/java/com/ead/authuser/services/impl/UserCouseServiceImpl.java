package com.ead.authuser.services.impl;

import com.ead.authuser.repository.UserCourseRepository;
import com.ead.authuser.services.UserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCouseServiceImpl implements UserCourseService {

    @Autowired
    UserCourseRepository userCourseRepository;
}
