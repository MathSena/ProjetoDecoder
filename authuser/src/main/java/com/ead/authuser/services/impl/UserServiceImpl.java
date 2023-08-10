package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> findAll() {
        logger.info("Fetching all users from the database");
        List<UserModel> users = userRepository.findAll();
        logger.info("Fetched {} users from the database", users.size());
        return users;
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        logger.info("Fetching user with ID: {}", userId);
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isPresent()) {
            logger.info("Found user with ID: {}", userId);
        } else {
            logger.info("User with ID: {} not found", userId);
        }
        return user;
    }

    @Override
    public void delete(UserModel userModel) {
        if (userModel != null && userModel.getUserId() != null) {
            logger.info("Deleting user with ID: {}", userModel.getUserId());
            userRepository.delete(userModel);
            logger.info("Deleted user with ID: {}", userModel.getUserId());
        } else {
            logger.info("Attempted to delete a null user or user with null ID");
        }
    }
}

