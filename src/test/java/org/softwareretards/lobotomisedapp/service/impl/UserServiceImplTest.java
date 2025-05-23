package org.softwareretards.lobotomisedapp.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.user.*;
import org.softwareretards.lobotomisedapp.mapper.user.UserMapper;
import org.softwareretards.lobotomisedapp.repository.user.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private CommitteeRepository committeeRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDto userDto;
    private Student student;
    private Company company;
    private Professor professor;
    private Committee committee;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setUsername("testuser");
        userDto.setPassword("password");
        userDto.setRole(Role.STUDENT);

        student = new Student();
        student.setUsername("testuser");
        student.setPassword("encodedPassword");
        student.setRole(Role.STUDENT);

        company = new Company();
        company.setUsername("companyuser");
        company.setPassword("encodedPassword");
        company.setRole(Role.COMPANY);

        professor = new Professor();
        professor.setUsername("professoruser");
        professor.setPassword("encodedPassword");
        professor.setRole(Role.PROFESSOR);

        committee = new Committee();
        committee.setUsername("committeeuser");
        committee.setPassword("encodedPassword");
        committee.setRole(Role.COMMITTEE);
    }

    @Test
    void createUser_UsernameTaken_ShouldThrowException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(student));

        assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
    }

    @Test
    void getProfile_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getProfile("testuser"));
    }

    @Test
    void findByUsername_UserNotFound_ShouldReturnNull() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        UserDto result = userService.findByUsername("testuser");

        assertNull(result);
    }

    @Test
    void changePassword_ValidInput_ShouldUpdatePassword() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedOldPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userService.changePassword("testuser", "currentPassword", "newPassword", "newPassword");

        verify(userRepository).save(user);
        assertEquals("encodedNewPassword", user.getPassword());
    }

    @Test
    void changePassword_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userService.changePassword("testuser", "currentPassword", "newPassword", "newPassword"));
    }

    @Test
    void changePassword_WrongCurrentPassword_ShouldThrowException() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedOldPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedOldPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                userService.changePassword("testuser", "wrongPassword", "newPassword", "newPassword"));
    }

    @Test
    void changePassword_PasswordsDontMatch_ShouldThrowException() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedOldPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "encodedOldPassword")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                userService.changePassword("testuser", "currentPassword", "newPassword", "differentPassword"));
    }

    @Test
    void changePassword_SameAsCurrentPassword_ShouldThrowException() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedOldPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.matches("newPassword", "encodedOldPassword")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                userService.changePassword("testuser", "currentPassword", "newPassword", "newPassword"));
    }
}