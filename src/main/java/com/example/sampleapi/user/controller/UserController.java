package com.example.sampleapi.user.controller;

import com.example.sampleapi.user.dto.UserCreateRequest;
import com.example.sampleapi.user.dto.UserResponse;
import com.example.sampleapi.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Vulnerability: SQL Injection endpoint
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam("name") String name) {
        return ResponseEntity.ok(userService.searchUsersByNameUnsafe(name));
    }

    // Vulnerability: Path Traversal (Arbitrary File Read)
    @GetMapping("/files")
    public ResponseEntity<String> readUserAttachment(@RequestParam("filePath") String filePath) throws IOException {
        File file = new File("/var/app/data/" + filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            return ResponseEntity.ok(new String(fis.readAllBytes()));
        }
    }

    // Vulnerability: Remote Command Execution / Command Injection
    @PostMapping("/diagnostics")
    public ResponseEntity<String> runDiagnostics(@RequestParam("cmd") String cmd) {
        return ResponseEntity.ok(userService.executeUserDiagnosticCommand(cmd));
    }
}
