package uz.pdp.online.lesson_5_task_1_hr_management_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Task;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.QueryDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.TaskDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.service.TaskService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @PostMapping("/add")
    public HttpEntity<?> addTask(@RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.addTask(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PatchMapping("/verifyTask")
    public HttpEntity<?> verifyTask(@RequestParam String taskCode, @RequestParam String email, @RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.verifyTask(taskCode, email, taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @PatchMapping("/edit/{id}")
    public HttpEntity<?> editTask(@PathVariable UUID id, @RequestBody TaskDto taskDto) {
        ApiResponse apiResponse = taskService.editTask(id, taskDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteTask(@PathVariable UUID id) {
        ApiResponse apiResponse = taskService.deleteTask(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getAllTask")
    public List<Task> getAllTask() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Task> allTask = taskService.getAllTask(authentication);
        return allTask;
    }

    @GetMapping("/byId/{id}")
    public Task getTaskById(@PathVariable UUID id) {
        Task oneTask = taskService.getOneTask(id);
        return oneTask;
    }

    @GetMapping("/query")
    public Set<Task> getTaskByQuery(@RequestBody QueryDto queryDto) {
        Set<Task> taskByQuery = taskService.getTaskByQuery(queryDto);
        return taskByQuery;
    }

}
