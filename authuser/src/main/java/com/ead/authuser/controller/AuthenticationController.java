package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
@Slf4j
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     *
     * <p>This endpoint is responsible for registering a new user based on the provided UserDto.
     * Before registering, it checks if the username and email are already in use. If either the
     * username or email is taken, it returns a conflict status. Otherwise, it creates a new user with
     * the provided details, sets the user status to ACTIVE, user type to STUDENT, and timestamps for
     * creation and last update to the current UTC time. After successfully registering the user, it
     * returns the created user model with a CREATED status.
     *
     * @param userDto The data transfer object containing user details for registration.
     * @return ResponseEntity containing the UserModel if successful, or an error message if there's a
     * conflict.
     */
    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(
            @RequestBody
            @JsonView(UserDto.UserView.RegistrationPost.class)
            @Validated(UserDto.UserView.RegistrationPost.class)
            UserDto userDto) {
        log.debug("Received signup request for user: {}", userDto.getUsername());
        if (userService.existsByUserName(userDto.getUsername())) {
            log.warn(
                    "Signup failed for username: {}. Reason: Username already exists", userDto.getUsername());
            return new ResponseEntity<>("Error: Username is already taken!", HttpStatus.CONFLICT);
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            log.warn("Signup failed for email: {}. Reason: Email already in use", userDto.getEmail());
            return new ResponseEntity<>("Error: Email is already in use!", HttpStatus.CONFLICT);
        }
        UserModel userModel = convertDtoToModel(userDto);
        log.info("Saving userModel for date: {}", userModel.getCreationDate());
        log.info("Saving userModel for username: {}", userModel.getUsername());
        userService.saveUser(userModel);
        log.info("User registered successfully with username: {}", userModel.getUsername());
        return new ResponseEntity<>(userModel, HttpStatus.CREATED);
    }

    private UserModel convertDtoToModel(UserDto userDto) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        LocalDateTime currentUtcTime = LocalDateTime.now(ZoneId.of("UTC"));
        userModel.setCreationDate(currentUtcTime);
        userModel.setLastUpdateDate(currentUtcTime);
        return userModel;
    }

    @GetMapping("/")
    public String index() {
        log.trace("TRACE");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");
        return "Logging Spring Boot...";
    }
}
