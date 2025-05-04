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
import org.softwareretards.lobotomisedapp.service.StudentService;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void showProfileFormShouldAddStudentProfileToModelAndReturnProfileFormView() {
        Model model = mock(Model.class);
        String username = "testStudent";
        StudentDto studentDto = new StudentDto();
        when(studentService.retrieveProfile(username)).thenReturn(studentDto);

        String viewName = studentController.showProfileForm(username, model);

        verify(model, times(1)).addAttribute("student", studentDto);
        assertEquals("students/profile-form", viewName);
    }

    //@Test
    //void saveProfileShouldSaveStudentProfileAndRedirectToProfilePage() {
    //    Model model = mock(Model.class);
    //    String username = "testStudent";
    //    StudentDto studentDto = new StudentDto();
    //    StudentDto savedProfile = new StudentDto();
    //    when(studentService.saveProfile(studentDto)).thenReturn(savedProfile);
//
    //    String viewName = studentController.saveProfile(username, studentDto, model);
//
    //    verify(model, times(1)).addAttribute("student", savedProfile);
    //    assertEquals("redirect:/students/" + username + "/profile", viewName);
    //}

    @Test
    void showApplicationFormShouldAddEmptyApplicationToModelAndReturnApplicationFormView() {
        Model model = mock(Model.class);
        String username = "testStudent";

        String viewName = studentController.showApplicationForm(username, model);

        verify(model, times(1)).addAttribute(eq("application"), any(TraineeshipApplicationDto.class));
        assertEquals("students/application-form", viewName);
    }

    @Test
    void submitApplicationShouldAddCreatedApplicationToModelAndReturnConfirmationView() {
        Model model = mock(Model.class);
        String username = "testStudent";
        TraineeshipApplicationDto applicationDto = new TraineeshipApplicationDto();
        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        positionDto.setId(1L);
        applicationDto.setPosition(positionDto);

        TraineeshipApplicationDto createdApplication = new TraineeshipApplicationDto();
        when(studentService.applyForTraineeship(username, applicationDto.getPosition().getId())).thenReturn(createdApplication);

        String viewName = studentController.submitApplication(username, applicationDto, model);

        verify(model, times(1)).addAttribute("application", createdApplication);
        assertEquals("students/application-confirmation", viewName);
    }

    @Test
    void showLogbookFormShouldAddEmptyLogbookEntryToModelAndReturnLogbookFormView() {
        Model model = mock(Model.class);
        String username = "testStudent";

        String viewName = studentController.showLogbookForm(username, model);

        verify(model, times(1)).addAttribute(eq("logbookEntry"), any(LogbookEntryDto.class));
        assertEquals("students/logbook-form", viewName);
    }

    @Test
    void saveLogbookEntryShouldAddSavedEntryToModelAndReturnConfirmationView() {
        Model model = mock(Model.class);
        String username = "testStudent";
        LogbookEntryDto logbookEntryDto = new LogbookEntryDto();
        TraineeshipPositionDto positionDto = new TraineeshipPositionDto();
        positionDto.setId(1L);
        logbookEntryDto.setPosition(positionDto);
        logbookEntryDto.setContent("Logbook content");

        LogbookEntryDto savedEntry = new LogbookEntryDto();
        when(studentService.saveLogbookEntry(username, logbookEntryDto.getPosition().getId(), logbookEntryDto.getContent()))
                .thenReturn(savedEntry);

        String viewName = studentController.saveLogbookEntry(username, logbookEntryDto, model);

        verify(model, times(1)).addAttribute("entry", savedEntry);
        assertEquals("students/logbook-confirmation", viewName);
    }
}
