package com.retailstore.discountservice;

import com.retailstore.discountservice.constant.UserType;
import com.retailstore.discountservice.model.User;
import com.retailstore.discountservice.repository.UserRepository;
import com.retailstore.discountservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void testCreateUser() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.EMPLOYEE);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.now());

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);

        assertEquals(savedUser.getId(), user.getId());
    }

    @Test
    public void testGetUser() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.EMPLOYEE);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.now());

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);

        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));

        User foundUser = userService.getUser(savedUser.getId());

        assertEquals(savedUser.getId(), foundUser.getId());
    }

    @Test
    public void testUpdateUser() {
        List<UserType> userTypes = List.of(UserType.CUSTOMER, UserType.EMPLOYEE);
        User user = new User(1L, "Test User", userTypes, LocalDateTime.now());

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);

        when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));

        User newUser = userService.getUser(savedUser.getId());
        newUser.setUsername("Test User 2");

        when(userRepository.save(newUser)).thenReturn(newUser);

        User updatedUser = userService.updateUser(newUser);

        assertEquals(newUser.getUsername(), updatedUser.getUsername());
    }
}
