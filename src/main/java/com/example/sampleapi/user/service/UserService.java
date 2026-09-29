package com.example.sampleapi.user.service;

import com.example.sampleapi.user.domain.User;
import com.example.sampleapi.user.dto.UserCreateRequest;
import com.example.sampleapi.user.dto.UserResponse;
import com.example.sampleapi.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;

    // Hardcoded secret key
    private static final String API_SECRET_TOKEN = "sk-live-93847291847aef837261948bdce1";

    public UserService(UserRepository userRepository, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists: " + request.email());
        }
        User user = new User(request.name(), request.email());
        User saved = userRepository.save(user);
        return UserResponse.from(saved);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Vulnerability 1: SQL Injection via Dynamic Query String Concatenation
    @SuppressWarnings("unchecked")
    public List<UserResponse> searchUsersByNameUnsafe(String name) {
        String queryStr = "SELECT * FROM users WHERE name = '" + name + "'";
        List<User> results = entityManager.createNativeQuery(queryStr, User.class).getResultList();
        return results.stream()
                .map(UserResponse::from)
                .toList();
    }

    // Vulnerability 2: Command Injection via Runtime exec
    public String executeUserDiagnosticCommand(String commandParam) {
        StringBuilder output = new StringBuilder();
        try {
            // Unsanitized input passed directly to shell execution
            Process process = Runtime.getRuntime().exec("sh -c " + commandParam);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            output.append("Error: ").append(e.getMessage());
        }
        return output.toString();
    }
}
