package uz.pdp.online.lesson_5_task_1_hr_management_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Salary;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.BetweenDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.SalaryDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.service.SalaryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @PostMapping("/add")
    public HttpEntity<?> addSalary(@RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.addSalary(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/viewAll")
    public List<Salary> salaryList() {
        List<Salary> allSalary = salaryService.getAllSalary();
        return allSalary;
    }

    @GetMapping("byId/{byId}")
    public Salary getSalaryById(@PathVariable UUID byId) {
        Salary salaryById = salaryService.getSalaryById(byId);
        return salaryById;
    }

    @GetMapping("byUserId/{byUserId}")
    public List<Salary> getListSalaryByUserId(@PathVariable UUID byUserId) {
        List<Salary> salaryByUserId = salaryService.getSalaryByUserId(byUserId);
        return salaryByUserId;
    }

    @GetMapping("/betweenMonth")
    public List<Salary> getListSalaryBetweenMonth(@RequestParam Integer start, @RequestParam Integer end) {
        List<Salary> salaryBetweenMonth = salaryService.getSalaryBetweenMonth(start,end);
        return salaryBetweenMonth;
    }

    @PutMapping("/{id}")
    public HttpEntity<?> editSalary(@PathVariable UUID id, @RequestBody SalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.editSalary(id, salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteSalaryById(@PathVariable UUID id) {
        ApiResponse apiResponse = salaryService.deleteSalaryById(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
