package com.ead.authuser.services.impl;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.enums.ActionType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.publishers.UserEventPublisher;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;


    @Autowired
    CourseClient courseClient;

    @Autowired
    UserEventPublisher userEventPublisher;

    @Override
    public List<UserModel> findAll() {
        log.info("Fetching all users from the database");
        List<UserModel> users = userRepository.findAll();
        log.info("Fetched {} users from the database", users.size());
        return users;
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        log.info("Fetching user with ID: {}", userId);
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isPresent()) {
            log.info("Found user with ID: {}", userId);
        } else {
            log.info("User with ID: {} not found", userId);
        }
        return user;
    }

    @Transactional
    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public void save(UserModel userModel) {
        log.info("Saving user with username: {}", userModel.getUsername());
        userRepository.save(userModel);
        log.info("User with username: {} saved successfully.", userModel.getUsername());
    }

    @Override
    public boolean existsByUserName(String username) {
        log.info("Checking if username: {} exists.", username);
        boolean exists = userRepository.existsByUsername(username);
        log.info("Username: {} exists: {}", username, exists);
        return exists;
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("Checking if email: {} exists.", email);
        boolean exists = userRepository.existsByEmail(email);
        log.info("Email: {} exists: {}", email, exists);
        return exists;
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        log.info("Fetching all users from the database");
        return userRepository.findAll(spec, pageable);
    }


    @Transactional
    @Override
    public UserModel saveUser(UserModel user) {
        log.info("Saving user with username: {}", user.getUsername());
        UserModel savedUser = userRepository.save(user);
        userEventPublisher.publishUserEvent(savedUser.convertToUserEventDto(), ActionType.CREATE);
        log.info("User with username: {} saved successfully.", user.getUsername());
        return savedUser;
    }
}

