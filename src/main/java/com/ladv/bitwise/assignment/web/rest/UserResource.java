package com.ladv.bitwise.assignment.web.rest;

import com.ladv.bitwise.assignment.service.UserService;
import com.ladv.bitwise.assignment.service.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserResource {
    private Logger log = LoggerFactory.getLogger(UserResource.class);

    private UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO user) throws URISyntaxException {
        log.debug("REST request to create user : {}", user);
        UserDTO createdUser = userService.save(user);
        return ResponseEntity.created(new URI("/api/users/" + createdUser.getId()))
                .body(createdUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        log.debug("REST request to get all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") Long id) {
        log.debug("REST request to get user by id : {}", id);
        return ResponseEntity.ok(userService.getUserById(id));

    }


}
