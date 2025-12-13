package com.spring_ecom.user.controllers;

import com.spring_ecom.user.dto.UserRequest;
import com.spring_ecom.user.dto.UserResponse;
import com.spring_ecom.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

public class UserController {


    private final UserService userService;

    @GetMapping("/api/users")
    public ResponseEntity <List<UserResponse>> getAllUsers(){
        return new ResponseEntity<>(userService.fetchAllUsers(),
                HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity< UserResponse> getUser(@PathVariable Long id){

        return  userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> getAllUsers(@RequestBody UserRequest userRequest){
        userService.addUser(userRequest);
        return new ResponseEntity<>("User added successfullu", HttpStatus.OK);
    }

    @PutMapping("/api/users/{id}")
    public ResponseEntity<String> getAllUsers(@PathVariable Long id,
                                              @RequestBody UserRequest updateUserRequest){
        boolean updated = userService.updateUser(id, updateUserRequest);
        if(updated){
            return  ResponseEntity.ok("User updated successfully");
        }
        return  ResponseEntity.notFound().build();
    }
}
