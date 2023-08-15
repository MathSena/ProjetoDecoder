package com.ead.authuser.services.impl;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @Override
    public void save(UserModel userModel) {
        logger.info("Saving user with username: {}", userModel.getUsername());
        userRepository.save(userModel);
        logger.info("User with username: {} saved successfully.", userModel.getUsername());
    }

    @Override
    public boolean existsByUserName(String username) {
        logger.info("Checking if username: {} exists.", username);
        boolean exists = userRepository.existsByUsername(username);
        logger.info("Username: {} exists: {}", username, exists);
        return exists;
    }

    @Override
    public boolean existsByEmail(String email) {
        logger.info("Checking if email: {} exists.", email);
        boolean exists = userRepository.existsByEmail(email);
        logger.info("Email: {} exists: {}", email, exists);
        return exists;
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        logger.info("Fetching all users from the database");
        return userRepository.findAll(spec, pageable);
    }
}

