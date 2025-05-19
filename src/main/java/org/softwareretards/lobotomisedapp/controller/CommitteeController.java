package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.EvaluationDto;
import org.softwareretards.lobotomisedapp.dto.traineeship.TraineeshipPositionDto;
import org.softwareretards.lobotomisedapp.dto.user.CommitteeDto;
import org.softwareretards.lobotomisedapp.dto.user.ProfessorDto;
import org.softwareretards.lobotomisedapp.dto.user.StudentDto;
import org.softwareretards.lobotomisedapp.entity.Evaluation;
import org.softwareretards.lobotomisedapp.entity.enums.FinalMark;
import org.softwareretards.lobotomisedapp.entity.user.Committee;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.mapper.EvaluationMapper;
import org.softwareretards.lobotomisedapp.mapper.traineeship.TraineeshipPositionMapper;
import org.softwareretards.lobotomisedapp.mapper.user.ProfessorMapper;
import org.softwareretards.lobotomisedapp.mapper.user.StudentMapper;
import org.softwareretards.lobotomisedapp.repository.EvaluationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipApplicationRepository;
import org.softwareretards.lobotomisedapp.repository.traineeship.TraineeshipPositionRepository;
import org.softwareretards.lobotomisedapp.repository.user.ProfessorRepository;
import org.softwareretards.lobotomisedapp.repository.user.StudentRepository;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;
import org.softwareretards.lobotomisedapp.service.CommitteeService;
import org.softwareretards.lobotomisedapp.service.RecommendationsService;
import org.softwareretards.lobotomisedapp.service.SearchService;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.softwareretards.lobotomisedapp.strategy.recommendation.RecommendationType;
import org.softwareretards.lobotomisedapp.strategy.search.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/committees")
public class CommitteeController {

    private final CommitteeService committeeService;
    private final TraineeshipPositionRepository traineeshipPositionRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RecommendationsService recommendationsService;
    private final TraineeshipApplicationRepository traineeshipApplicationRepository;
    private final SearchService searchService;
    private final EvaluationRepository evaluationRepository;

    @Autowired
    public CommitteeController(CommitteeService committeeService,
                               TraineeshipPositionRepository traineeshipPositionRepository,
                               ProfessorRepository professorRepository,
                               StudentRepository studentRepository,
                               UserService userService,
                               UserRepository userRepository,
                               RecommendationsService recommendationsService,
                               TraineeshipApplicationRepository traineeshipApplicationRepository,
                               SearchService searchService,
                               EvaluationRepository evaluationRepository) {
        this.committeeService = committeeService;
        this.traineeshipPositionRepository = traineeshipPositionRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.recommendationsService = recommendationsService;
        this.traineeshipApplicationRepository = traineeshipApplicationRepository;
        this.searchService = searchService;
        this.evaluationRepository = evaluationRepository;
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
    public String assignSupervisingProfessor(
            @RequestParam Long committeeId,
            @RequestParam Long traineeshipId,
            @RequestParam Long professorId,
            RedirectAttributes redirectAttributes) {
        try {
            committeeService.assignSupervisingProfessor(committeeId, traineeshipId, professorId);
            redirectAttributes.addFlashAttribute("success", "Professor assigned successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/committees/professor-list?professorId=" + professorId;
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
    public String showProfessorList(
            @RequestParam(value = "professorId", required = false) Long professorId,
            @RequestParam(value = "strategy", defaultValue = "NONE") SearchType strategy,
            Model model,
            Authentication authentication) {

        // Get committee
        String username = authentication.getName();
        CommitteeDto committeeDto = committeeService.getCommitteeById(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"))
                        .getId()
        );
        model.addAttribute("committee", committeeDto);

        // Get all professors without workload
        List<ProfessorDto> professors = professorRepository.findAll().stream()
                .map(ProfessorMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("professors", professors);

        if (professorId != null) {
            ProfessorDto selectedProfessor = professorRepository.findById(professorId)
                    .map(ProfessorMapper::toDto)
                    .orElse(null);

            // Calculate workload live using repository query
            Integer workload = traineeshipPositionRepository.countPositionsByProfessorId(professorId);

            //List<TraineeshipPositionDto> recommendations =
            //        searchService.recommend(positionId, strategy);

            model.addAttribute("selectedProfessor", selectedProfessor);
            //model.addAttribute("recommendations", recommendations);
            model.addAttribute("selectedStrategy", strategy);
            model.addAttribute("workload", workload);
        }

        return "committees/professor-list";
    }

    //TODO: FIX THE ABOVE CONTROLLER
    @GetMapping("/student-list")
    public String showStudentList(
            @RequestParam(value = "studentId", required = false) Long studentId,
            @RequestParam(value = "strategy", defaultValue = "NONE") RecommendationType strategy,
            Model model,
            Authentication authentication) {

        // Get the currently logged-in committee
        String username = authentication.getName();
        CommitteeDto committeeDto = committeeService.getCommitteeById(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"))
                        .getId()
        );

        // Add committee to model
        model.addAttribute("committee", committeeDto);

        // Get all students with applications
        List<Long> studentIds = traineeshipApplicationRepository.findDistinctStudentIds();
        List<StudentDto> students = studentRepository.findAllById(studentIds).stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("students", students);

        if (studentId != null) {
            StudentDto selectedStudent = studentRepository.findById(studentId)
                    .map(StudentMapper::toDto)
                    .orElse(null);

            List<TraineeshipPositionDto> recommendations =
                    recommendationsService.recommend(studentId, strategy);

            model.addAttribute("selectedStudent", selectedStudent);
            model.addAttribute("recommendations", recommendations);
            model.addAttribute("selectedStrategy", strategy);
        }

        return "committees/student-list";
    }

    @GetMapping("/{username}/profile")
    public String showProfileForm(@PathVariable String username, Model model) {
        CommitteeDto committeeDto = committeeService.getCommitteeById(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"))
                        .getId()
        );
        model.addAttribute("committee", committeeDto);
        return "committees/profile-form";
    }

    @PostMapping("/{username}/profile")
    public String saveProfile(
            @PathVariable String username,
            @ModelAttribute("committee") CommitteeDto committeeDto,
            Model model) {

        // Get existing profile to preserve UserDto
        CommitteeDto existingProfile = committeeService.getCommitteeById(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"))
                        .getId()
        );

        // Merge changes
        committeeDto.setUserDto(existingProfile.getUserDto());

        CommitteeDto savedProfile = committeeService.updateCommittee(
                existingProfile.getUserDto().getId(),
                committeeDto
        );
        model.addAttribute("committee", savedProfile);
        return "redirect:/committees/" + username + "/profile";
    }

    @PostMapping("/{username}/change-password")
    public String changePassword(
            @PathVariable String username,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(username, currentPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/committees/" + username + "/profile";
    }

    @PostMapping("/assign-traineeship")
    public String assignTraineeship(@RequestParam Long committeeId,
                                    @RequestParam Long studentId,
                                    @RequestParam Long traineeshipId,
                                    RedirectAttributes redirectAttributes) {
        try {
            // Call your existing service method
            committeeService.assignTraineeshipToStudent(committeeId, studentId, traineeshipId);
            committeeService.acceptApplication(studentId);
            redirectAttributes.addFlashAttribute("success", "Traineeship assigned successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to assign traineeship: " + e.getMessage());
        }


        return "redirect:/committees/student-list?studentId=" + studentId;
    }

    @GetMapping("/{username}/traineeships/{positionId}/info")
    public String showEvaluationInfo(
            @PathVariable String username,
            @PathVariable Long positionId,
            @AuthenticationPrincipal User user,
            Model model) {

        // Authorization check
        if (!user.getUsername().equals(username)) {
            return "redirect:/access-denied";
        }

        // Get required data
        CommitteeDto committeeDto = committeeService.getCommitteeById(user.getId());
        List<TraineeshipPositionDto> positions = traineeshipPositionRepository.findAll().stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList());

        // Get evaluation data
        Evaluation evaluation = evaluationRepository.findByTraineeshipPositionId(positionId)
                .orElse(null);

        // Add all required attributes
        model.addAttribute("committee", committeeDto);
        model.addAttribute("positions", traineeshipPositionRepository.findAll().stream()
                .map(TraineeshipPositionMapper::toDto)
                .collect(Collectors.toList()));
        model.addAttribute("evaluation", evaluation);
        model.addAttribute("position", traineeshipPositionRepository.findById(positionId).orElseThrow());

        return "committees/dashboard";
    }

    @PostMapping("/{username}/update-final-mark")
    public String updateFinalMark(@PathVariable String username,
                                  @RequestParam Long positionId,
                                  @RequestParam FinalMark finalMark,
                                  RedirectAttributes redirectAttributes) {
        try {
            Evaluation evaluation = evaluationRepository.findByTraineeshipPositionId(positionId)
                    .orElseThrow(() -> new RuntimeException("Evaluation not found"));

            evaluation.setFinalMark(finalMark);
            evaluationRepository.save(evaluation);

            redirectAttributes.addFlashAttribute("success", "Final mark updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating final mark: " + e.getMessage());
        }
        return "redirect:/committees/" + username + "/dashboard";
    }

    @GetMapping("/committees/evaluations/{positionId}")
    @ResponseBody
    public EvaluationDto getEvaluationData(@PathVariable Long positionId) {
        return evaluationRepository.findByTraineeshipPositionId(positionId)
                .map(EvaluationMapper::toDto)
                .orElse(null);
    }
}
