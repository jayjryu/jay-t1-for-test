package com.example.sampleapi.user.service;

import com.example.sampleapi.user.domain.User;
import com.example.sampleapi.user.dto.UserCreateRequest;
import com.example.sampleapi.user.dto.UserResponse;
import com.example.sampleapi.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Create user successfully")
    void createUser_success() {
        UserCreateRequest request = new UserCreateRequest("John Doe", "john@example.com");
        User user = new User("John Doe", "john@example.com");

        given(userRepository.existsByEmail("john@example.com")).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(user);

        UserResponse response = userService.createUser(request);

        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Duplicate email throws exception")
    void createUser_duplicateEmail_throwsException() {
        UserCreateRequest request = new UserCreateRequest("John Doe", "john@example.com");
        given(userRepository.existsByEmail("john@example.com")).willReturn(true);

        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already exists");
    }

    @Test
    @DisplayName("Find user by non-existing id throws exception")
    void getUserById_notFound_throwsException() {
        given(userRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }
}
