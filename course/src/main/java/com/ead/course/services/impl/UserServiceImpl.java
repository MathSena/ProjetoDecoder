package com.ead.course.services.impl;


import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        log.info("Searching for users with the following filters: {}", spec);
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public UserModel save(UserModel userModel) {
        log.info("Saving user: {}", userModel);
        return userRepository.save(userModel);
    }

    @Override
    public UserModel delete(UUID userId) {
        log.info("Deleting user with ID: {}", userId);
        var userModel = userRepository.findById(userId)
                .orElseThrow();
        userRepository.delete(userModel);
        return userModel;

    }

    @Override
    public Optional<UserModel> findById(UUID userInstructor) {
        return userRepository.findById(userInstructor);
    }
}
