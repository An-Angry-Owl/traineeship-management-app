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

import java.util.List;

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

    @Test
    void showProfileFormShouldAddStudentToModelAndReturnProfileFormView() {
        Model model = mock(Model.class);
        String username = "student1";
        StudentDto studentDto = new StudentDto();
        when(studentService.retrieveProfile(username)).thenReturn(studentDto);

        String viewName = studentController.showProfileForm(username, model);

        verify(model).addAttribute("student", studentDto);
        assertEquals("students/profile-form", viewName);
    }

    @Test
    void saveProfileShouldAddSavedProfileToModelAndRedirectToProfile() {
        Model model = mock(Model.class);
        String username = "student1";
        StudentDto inputDto = new StudentDto();
        StudentDto savedDto = new StudentDto();
        when(studentService.saveProfile(username, inputDto)).thenReturn(savedDto);

        String viewName = studentController.saveProfile(username, inputDto, model);

        verify(model).addAttribute("student", savedDto);
        assertEquals("redirect:/students/" + username + "/profile", viewName);
    }

    @Test
    void submitApplicationShouldRedirectWithSuccessWhenServiceSucceeds() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String username = "student1";
        Long positionId = 2L;
        when(studentService.applyForTraineeship(username, positionId)).thenReturn(new TraineeshipApplicationDto());

        String viewName = studentController.submitApplication(username, positionId, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("success", "Application submitted successfully!");
        assertEquals("redirect:/students/" + username + "/traineeships", viewName);
    }

    @Test
    void showLogbookFormShouldReturnAccessDeniedIfUserMismatch() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("otherStudent");

        String viewName = studentController.showLogbookForm("student1", user, model);

        assertEquals("access-denied", viewName);
    }

    @Test
    void showLogbookFormShouldAddStudentAndTraineeshipToModelAndReturnForm() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("student1");
        StudentDto studentDto = new StudentDto();
        TraineeshipPositionDto traineeship = new TraineeshipPositionDto();
        when(studentService.retrieveProfile("student1")).thenReturn(studentDto);
        when(studentService.getCurrentTraineeship("student1")).thenReturn(traineeship);

        String viewName = studentController.showLogbookForm("student1", user, model);

        verify(model).addAttribute("currentTraineeship", traineeship);
        verify(model).addAttribute("student", studentDto);
        assertEquals("students/logbook-form", viewName);
    }

    @Test
    void saveLogbookEntryShouldReturnAccessDeniedIfUserMismatch() {
        User user = new User();
        user.setUsername("otherStudent");
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String viewName = studentController.saveLogbookEntry("student1", user, "content", 1L, redirectAttributes);

        assertEquals("access-denied", viewName);
    }

    @Test
    void saveLogbookEntryShouldRedirectWithSuccessWhenServiceSucceeds() {
        User user = new User();
        user.setUsername("student1");
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String viewName = studentController.saveLogbookEntry("student1", user, "content", 1L, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("success", "Logbook entry saved successfully!");
        assertEquals("redirect:/students/student1/logbook", viewName);
    }

    @Test
    void changePasswordShouldRedirectWithSuccessWhenServiceSucceeds() {
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        String viewName = studentController.changePassword("student1", "old", "new", "new", redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("success", "Password changed successfully!");
        assertEquals("redirect:/students/student1/profile", viewName);
    }

    @Test
    void showDashboardShouldAddStudentAndTraineeshipToModelAndReturnDashboard() {
        Model model = mock(Model.class);
        String username = "student1";
        StudentDto studentDto = new StudentDto();
        TraineeshipPositionDto traineeship = new TraineeshipPositionDto();
        when(studentService.retrieveProfile(username)).thenReturn(studentDto);
        when(studentService.getCurrentTraineeship(username)).thenReturn(traineeship);

        String viewName = studentController.showDashboard(username, model);

        verify(model).addAttribute("student", studentDto);
        verify(model).addAttribute("currentTraineeship", traineeship);
        assertEquals("students/dashboard", viewName);
    }

    @Test
    void showAvailableTraineeshipsShouldReturnAccessDeniedIfUserMismatch() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("otherStudent");

        String viewName = studentController.showAvailableTraineeships("student1", user, model);

        assertEquals("access-denied", viewName);
    }

    @Test
    void showAvailableTraineeshipsShouldAddPositionsStudentAndApplicationToModel() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("student1");
        List<TraineeshipPositionDto> positions = List.of(new TraineeshipPositionDto());
        StudentDto studentDto = new StudentDto();
        when(studentService.getOpenTraineeshipPositions()).thenReturn(positions);
        when(studentService.retrieveProfile("student1")).thenReturn(studentDto);

        String viewName = studentController.showAvailableTraineeships("student1", user, model);

        verify(model).addAttribute("positions", positions);
        verify(model).addAttribute("student", studentDto);
        verify(model).addAttribute(eq("application"), any(TraineeshipApplicationDto.class));
        assertEquals("students/traineeship-list", viewName);
    }

    @Test
    void viewLogbookEntriesShouldAddStudentTraineeshipAndEntriesToModelAndReturnEntriesView() {
        Model model = mock(Model.class);
        User user = new User();
        user.setUsername("student1");
        StudentDto studentDto = new StudentDto();
        TraineeshipPositionDto traineeship = new TraineeshipPositionDto();
        traineeship.setId(5L);
        List<LogbookEntryDto> entries = List.of(new LogbookEntryDto());
        when(studentService.retrieveProfile("student1")).thenReturn(studentDto);
        when(studentService.getCurrentTraineeship("student1")).thenReturn(traineeship);
        when(studentService.getLogbookEntries("student1", 5L)).thenReturn(entries);

        String viewName = studentController.viewLogbookEntries("student1", user, model);

        verify(model).addAttribute("student", studentDto);
        verify(model).addAttribute("currentTraineeship", traineeship);
        verify(model).addAttribute("entries", entries);
        assertEquals("students/logbook-entries", viewName);
    }


}
