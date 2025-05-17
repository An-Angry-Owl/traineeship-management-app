package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/committees")
public class CommitteeController {

    private final CommitteeService committeeService;
    private final TraineeshipPositionRepository traineeshipPositionRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public CommitteeController(CommitteeService committeeService, TraineeshipPositionRepository traineeshipPositionRepository, ProfessorRepository professorRepository, StudentRepository studentRepository) {
        this.committeeService = committeeService;
        this.traineeshipPositionRepository = traineeshipPositionRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/list")
    public String getAllCommittees(Model model) {
        model.addAttribute("committees", committeeService.getAllCommittees());
        return "committee/list";
    }

    @GetMapping("/view/{id}")
    public String getCommitteeById(@PathVariable Long id, Model model) {
        CommitteeDto committee = committeeService.getCommitteeById(id);
        model.addAttribute("committee", committee);
        return "committee/view";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("committeeDto", new CommitteeDto());
        return "committee/create";
    }

    @PostMapping("/create")
    public String createCommittee(@ModelAttribute CommitteeDto committeeDto) {
        committeeService.createCommittee(committeeDto);
        return "redirect:/committee/list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        CommitteeDto committee = committeeService.getCommitteeById(id);
        model.addAttribute("committeeDto", committee);
        return "committee/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCommittee(@PathVariable Long id,
                                  @ModelAttribute CommitteeDto committeeDto) {
        committeeService.updateCommittee(id, committeeDto);
        return "redirect:/committee/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCommittee(@PathVariable Long id) {
        committeeService.deleteCommittee(id);
        return "redirect:/committee/list";
    }

    @PostMapping("/assignTraineeship")
    public String assignTraineeshipToStudent(@RequestParam Long committeeId,
                                             @RequestParam Long studentId,
                                             @RequestParam Long traineeshipId) {
        committeeService.assignTraineeshipToStudent(committeeId, studentId, traineeshipId);
        return "redirect:/committee/list";
    }

    @PostMapping("/assignProfessor")
    public String assignSupervisingProfessor(@RequestParam Long committeeId,
                                             @RequestParam Long traineeshipId,
                                             @RequestParam Long professorId) {
        committeeService.assignSupervisingProfessor(committeeId, traineeshipId, professorId);
        return "redirect:/committee/list";
    }

    @GetMapping("/monitorEvaluations/{traineeshipId}")
    public String monitorTraineeshipEvaluations(@PathVariable Long traineeshipId,
                                                Model model) {
        committeeService.monitorTraineeshipEvaluations(null, traineeshipId);
        model.addAttribute("traineeshipId", traineeshipId);
        return "committee/monitorEvaluations";
    }

    @GetMapping("/{username}/dashboard")
    public String showDashboard(
            @PathVariable String username,
            @AuthenticationPrincipal User user,
            Model model) {

        // Authorization check
        if (!user.getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        CommitteeDto committeeDto = committeeService.getCommitteeById(user.getId());
        List<TraineeshipPositionDto> positions = traineeshipPositionRepository.findAll().stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("committee", committeeDto);
        model.addAttribute("positions", positions);

        return "committees/dashboard";
    }

    @GetMapping("/professor-list")
    public String showProfessorList(Model model) {
        model.addAttribute("professors", professorRepository.findAll());
        return "committees/professor-list";
    }

    @GetMapping("/student-list")
    public String showStudentList(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "committees/student-list";
    }
}
