package uz.pdp.online.lesson_5_task_1_hr_management_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Task;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.TaskState;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.RoleName;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.State;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.QueryDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.TaskDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.TaskRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.TaskStateRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.UserRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.service.emailService.CustomEmailService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    TaskRepos taskRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    TaskStateRepos taskStateRepos;
    @Autowired
    CustomEmailService customEmailService;

    public ApiResponse addTask(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_EMPLOYEE.name()))
                return new ApiResponse("Xodim vazifa qo'sha olmaydi", false);
        }

        String currentUser = "";
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name()))
                currentUser = authority.getAuthority();
            if (authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))
                currentUser = authority.getAuthority();
        }

        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setTaskState(taskStateRepos.getById(1));
        task.setTaskCode(UUID.randomUUID().toString());

        Optional<User> optionalUser = userRepos.findByEmail(taskDto.getEmployeeEmail());
        if (!optionalUser.isPresent())
            return new ApiResponse("Vazifa kimga berilayotgani aniqlanmadi", false);

        User userByEmail = optionalUser.get();
        boolean givenEmailIsHRManager = false;
        boolean givenEmailIsEmployee = false;
        for (GrantedAuthority authority : userByEmail.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_EMPLOYEE.name()))
                givenEmailIsEmployee = true;
            if (authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))
                givenEmailIsHRManager = true;
        }

        if (currentUser.equals(RoleName.ROLE_DIRECTOR.name()) && (givenEmailIsEmployee || givenEmailIsHRManager))
            task.setEmployeeEmail(userByEmail.getEmail());
        if (currentUser.equals(RoleName.ROLE_HR_MANAGER.name()) && givenEmailIsEmployee) {
            task.setEmployeeEmail(userByEmail.getEmail());
        } else {
            return new ApiResponse("Manager faqat xodimlarga vazifa biriktira oladi", false);
        }

        Timestamp deadlineDate = new Timestamp(taskDto.getDeadLineDate());
        task.setDeadLineTime(deadlineDate);

        taskRepos.save(task);
        customEmailService.sendEmail(task.getEmployeeEmail(), task.getTaskCode(), "verifyTask", task.getName(), task.getDescription());
        return new ApiResponse("Vazifa saqlandi va xodimning e-pochtasiga havola yuborildi", true);
    }


    public ApiResponse verifyTask(String taskCode, String email, TaskDto taskDto) {// taskDto.getTaskStateId = given
        Optional<Task> byEmployeeEmailAndTaskCode = taskRepos.findByEmployeeEmailAndTaskCode(email, taskCode);
        if (!byEmployeeEmailAndTaskCode.isPresent())
            return new ApiResponse("Vazifa topilmadi yoki allaqachon tasdiqlangan", false);

        Optional<TaskState> optionalTaskState = taskStateRepos.findById(2);
        if (!optionalTaskState.isPresent())
            return new ApiResponse("Vazifa bajarilish jarayonida", false);

        Task task = byEmployeeEmailAndTaskCode.get();
        task.setTaskState(optionalTaskState.get());
        task.setTaskCode(null);
        taskRepos.save(task);

        UUID createdBy = task.getCreatedBy();
        Optional<User> optionalCreatedUser = userRepos.findById(createdBy);
        if (!optionalCreatedUser.isPresent())
            return new ApiResponse("Vazifaning egasi topilmadi", false);
        User taskCreatedByThisUser = optionalCreatedUser.get();
        String taskCreatedByThisUserEmail = taskCreatedByThisUser.getEmail();

        customEmailService.sendEmail(taskCreatedByThisUserEmail, task.getName(), task.getEmployeeEmail());
        return new ApiResponse("Vazifa qabul qilindi, statusi - " + task.getTaskState(), true);
    }


    public ApiResponse editTask(UUID id, TaskDto taskDto) {//taskDto.getTaskState = given only
        Optional<Task> optionalTask = taskRepos.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Ushbu id lik vazifa topilmadi", false);

        Optional<TaskState> optionalTaskState = taskStateRepos.findById(taskDto.getTaskStateId());
        if (!optionalTaskState.isPresent())
            return new ApiResponse("Vazifaning qay holatda ekanligi topilmadi", false);

        Task task = optionalTask.get();
        User createdUser = userRepos.getById(task.getCreatedBy());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authUser = (User) authentication.getPrincipal();
        if (!authUser.getUsername().equals(task.getEmployeeEmail())
                || authUser.getUsername().equals(createdUser.getUsername()))
            return new ApiResponse("Vazifani faqat ushbu vazifani bajaruvchi xodim tahrirlay oladi ", false);

        if (taskDto.getTaskStateId() == 3) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (task.getDeadLineTime().after(timestamp)) {
                task.setCompletedOnTime(true);
                task.setCompleted(true);
            } else {
                task.setCompletedOnTime(false);
                task.setCompleted(true);
                task.setTaskState(taskStateRepos.findByState(State.COMPLETED));
            }
        } else {
            task.setTaskState(optionalTaskState.get());
            task.setCompleted(false);
            task.setCompletedOnTime(false);
        }

        String createdUserTask = createdUser.getUsername();

        taskRepos.save(task);
        customEmailService.sendEmail(createdUserTask, task.getEmployeeEmail(), task.getName(), task.getTaskState().toString());
        return new ApiResponse("Vazifa biriktirilgan xodimning e-pochtasiga yuborildi", true);

    }


    public ApiResponse deleteTask(UUID id) {
        Optional<Task> optionalTask = taskRepos.findById(id);
        if (!optionalTask.isPresent())
            return new ApiResponse("Ushbu id lik vazifa MO dan topilmadi", false);

        Task task = optionalTask.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticationUser = (User) authentication.getPrincipal();

        String authenticationUserUsername = authenticationUser.getUsername();

        Optional<User> optionalUser = userRepos.findById(task.getCreatedBy());
        User createdUser = optionalUser.get();
        String createdUserUsername = createdUser.getUsername();

        String employeeEmail = task.getEmployeeEmail();

        if (!authenticationUserUsername.equals(createdUserUsername) && !authenticationUserUsername.equals(employeeEmail))
            return new ApiResponse("Token xato berildi", false);

        taskRepos.delete(task);
        return new ApiResponse("task deleted", true);
    }


    public List<Task> getAllTask(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())
                    || authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))
                return taskRepos.findAll();
        }
        return null;
    }

    public Task getOneTask(UUID id) {

        Optional<Task> optionalTask = taskRepos.findById(id);
        if (!optionalTask.isPresent())
            return null;

        Task task = optionalTask.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name())
                    || authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name()))
            return task;
        }
        return null;
    }

    public Set<Task> getTaskByQuery(QueryDto queryDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (!authority.getAuthority().equals(RoleName.ROLE_EMPLOYEE.name())) {
                return taskRepos.findAllByEmployeeEmailAndCompletedAndCompletedOnTime(queryDto.getEmail(), queryDto.isCompleted(),queryDto.isCompletedOnTime());
            }
        }
        return null;
    }
}
