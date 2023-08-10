package com.ead.authuser.controller;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}


