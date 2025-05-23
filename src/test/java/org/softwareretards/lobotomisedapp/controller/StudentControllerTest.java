package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.LogbookEntryDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipApplicationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.service.StudentService;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private UserService userService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void saveProfileShouldThrowExceptionWhenStudentServiceThrows() {
        Model model = mock(Model.class);
        String username = "testStudent";
        StudentDto studentDto = new StudentDto();
        when(studentService.saveProfile(username, studentDto)).thenThrow(new RuntimeException("fail"));

        assertThrows(RuntimeException.class, () -> studentController.saveProfile(username, studentDto, model));
    }

    @Test
    void submitApplicationShouldRedirectWithErrorWhenServiceThrows() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String username = "testStudent";
        Long positionId = 1L;
        doThrow(new RuntimeException("error")).when(studentService).applyForTraineeship(username, positionId);

        String viewName = studentController.submitApplication(username, positionId, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "error");
        assertEquals("redirect:/students/" + username + "/traineeships", viewName);
    }

    @Test
    void showApplicationFormShouldAddEmptyApplicationToModelAndReturnApplicationFormView() {
        Model model = mock(Model.class);
        String username = "testStudent";

        String viewName = studentController.showApplicationForm(username, model);

        verify(model, times(1)).addAttribute(eq("application"), any(TraineeshipApplicationDto.class));
        assertEquals("students/application-form", viewName);
    }

    @Test
    void saveLogbookEntryShouldRedirectWithErrorWhenServiceThrows() {
        User user = new User();
        user.setUsername("testStudent");
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doThrow(new RuntimeException("fail")).when(studentService).saveLogbookEntry("testStudent", 1L, "content");

        String viewName = studentController.saveLogbookEntry("testStudent", user, "content", 1L, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "fail");
        assertEquals("redirect:/students/testStudent/logbook", viewName);
    }

    @Test
    void changePasswordShouldRedirectWithErrorWhenServiceThrows() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        doThrow(new RuntimeException("fail")).when(userService).changePassword("testStudent", "old", "new", "new");

        String viewName = studentController.changePassword("testStudent", "old", "new", "new", redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("error", "fail");
        assertEquals("redirect:/students/testStudent/profile", viewName);
    }

    @Test
    void showLogbookFormShouldRedirectToDashboardIfNoCurrentTraineeship() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("testStudent");
        when(studentService.retrieveProfile("testStudent")).thenReturn(new StudentDto());
        when(studentService.getCurrentTraineeship("testStudent")).thenReturn(null);

        String viewName = studentController.showLogbookForm("testStudent", user, model);

        assertEquals("redirect:/students/testStudent/dashboard", viewName);
    }

    @Test
    void viewLogbookEntriesShouldRedirectToDashboardIfNoCurrentTraineeship() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("testStudent");
        when(studentService.retrieveProfile("testStudent")).thenReturn(new StudentDto());
        when(studentService.getCurrentTraineeship("testStudent")).thenReturn(null);

        String viewName = studentController.viewLogbookEntries("testStudent", user, model);

        assertEquals("redirect:/students/testStudent/dashboard", viewName);
    }

    @Test
    void viewLogbookEntriesShouldReturnAccessDeniedIfUserMismatch() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("otherUser");
        assertEquals("access-denied", studentController.viewLogbookEntries("testStudent", user, model));
    }

}
