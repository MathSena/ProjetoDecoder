package com.ead.authuser.controller;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins="*", maxAge=3600)
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieve all users.
     *
     * @return List of UserModel
     */
    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    /**
     * Retrieve a single user by ID.
     *
     * @param userId The ID of the user to retrieve
     * @return UserModel or error message
     */
    // TODO -  Review the logic (Maybe create a APIResponse class)
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value="userId") UUID userId) {
        Optional<UserModel> userModel = userService.findById(userId);
        return userModel.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    /**
     * Delete a user by ID.
     *
     * @param userId The ID of the user to delete
     * @return Success or error message
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteOneUser(@PathVariable UUID userId) {
        return userService.findById(userId).map(user -> {
            userService.delete(user);
            return ResponseEntity.ok("User deleted successfully");
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    /**
     * Updates user details such as full name, phone number, and CPF (Taxpayer ID number).
     *
     * @param userId   The ID of the user to be updated.
     * @param userDto  A UserDto object containing the details to be updated.
     * @return         ResponseEntity with the updated UserModel object.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserModel> updateUserDetails(@PathVariable(value = "userId") UUID userId,
                                                       @JsonView(UserDto.UserView.UserPut.class) @RequestBody UserDto userDto) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            UserModel userModel = userModelOptional.get();
            userModel.setFullName(userDto.getFullName());
            userModel.setPhoneNumber(userDto.getPhoneNumber());
            userModel.setCpf(userDto.getCpf());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.ok(userModel);
        }
    }

    /**
     * Updates the user's password.
     *
     * @param userId   The ID of the user to be updated.
     * @param userDto  A UserDto object containing the new password and old password.
     * @return         ResponseEntity with a message indicating the result of the operation.
     */
    @PutMapping("/{userId}/password")
    public ResponseEntity<String> updatePassword(@PathVariable(value = "userId") UUID userId,
                                                 @JsonView(UserDto.UserView.PasswordPut.class) @RequestBody UserDto userDto) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            UserModel userModel = userModelOptional.get();
            if (!userModel.getPassword().equals(userDto.getOldPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mismatched Old Password");
            }
            userModel.setPassword(userDto.getPassword());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.ok("Password Updated");
        }
    }

    /**
     * Updates the user's profile image.
     *
     * @param userId   The ID of the user to be updated.
     * @param userDto  A UserDto object containing the URL of the new image.
     * @return         ResponseEntity with the updated UserModel object.
     */
    @PutMapping("/{userId}/image")
    public ResponseEntity<UserModel> updateImage(@PathVariable(value = "userId") UUID userId,
                                                 @JsonView(UserDto.UserView.ImagePut.class) @RequestBody UserDto userDto) {
        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (userModelOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            UserModel userModel = userModelOptional.get();
            userModel.setImageUrl(userDto.getImageUrl());
            userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
            userService.save(userModel);
            return ResponseEntity.ok(userModel);
        }
    }

}


