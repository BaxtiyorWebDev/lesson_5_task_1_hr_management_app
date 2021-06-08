package uz.pdp.online.lesson_5_task_1_hr_management_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.InputOutput;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.BetweenDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.InputOutputDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.service.InputOutputService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inputOutput")
public class InputOutputController {

    @Autowired
    InputOutputService inputOutputService;

    @PostMapping("/input")
    public HttpEntity<?> addInput(@RequestBody InputOutputDto inputOutputDto) {
        ApiResponse apiResponse = inputOutputService.writeInput(inputOutputDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PostMapping("/output")
    public HttpEntity<?> addOutput(@RequestBody InputOutputDto inputOutputDto) {
        ApiResponse apiResponse = inputOutputService.writeOutput(inputOutputDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping("/getAll")
    public List<InputOutput> inputOutputList() {
        List<InputOutput> allInputOutputHistory = inputOutputService.getAllInputOutputHistory();
        return allInputOutputHistory;
    }

    @GetMapping("/{id}")
    public InputOutput getInputOutputById(@PathVariable UUID id) {
        InputOutput inputOutputById = inputOutputService.getInputOutputById(id);
        return inputOutputById;
    }

    @GetMapping("/betweenTime")
    public Object getInputOutputBetweenTime(@RequestBody BetweenDto betweenDto) {
        Object inputOutputBetweenTime = inputOutputService.getInputOutputBetweenTime(betweenDto);
        return inputOutputBetweenTime;
    }

}
