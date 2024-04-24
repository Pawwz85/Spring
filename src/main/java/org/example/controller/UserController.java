package org.example.controller;

import org.example.dto.CreateUserDto;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<Collection<UserDto>> getUsers() {
        Collection<UserDto> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{login}")
     public ResponseEntity<UserDto> getUser(@PathVariable String login) {
//    @GetMapping
//    public ResponseEntity<UserDto> getUser(@RequestParam(required = true) String login) {
        UserDto userDto = userService.getUser(login);
        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserDto createUserDto) {
        Boolean success = userService.createUser(createUserDto);

        if(success)
        return ResponseEntity.ok(("User created successfully"));
        else
            return ResponseEntity.badRequest().body("User already exist");
    }

    @DeleteMapping("{login}")
    public ResponseEntity<?> deleteUser(@PathVariable String login) {
        String result = userService.deleteUser(login);
        switch (result) {
            case "deleted":
                return ResponseEntity.ok().body(result);
            case "vehicle is not null":
                return ResponseEntity.badRequest().body(result);
            case "not found":
                return ResponseEntity.notFound().build();
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }


}