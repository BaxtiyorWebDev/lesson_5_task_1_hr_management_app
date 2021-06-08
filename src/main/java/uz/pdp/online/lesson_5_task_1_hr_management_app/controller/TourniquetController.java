package uz.pdp.online.lesson_5_task_1_hr_management_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Tourniquet;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.TourniquetDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.service.TourniquetService;

import java.util.List;

@RestController
@RequestMapping("/api/tourniquet")
public class TourniquetController {

    @Autowired
    TourniquetService tourniquetService;

    @PostMapping("/add")
    public HttpEntity<?> addTourniquet(@RequestBody TourniquetDto tourniquetDto) {
        ApiResponse apiResponse = tourniquetService.addTourniquet(tourniquetDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @PutMapping("/edit/{id}")
    public HttpEntity<?> editTourniquet(@PathVariable Integer id, @RequestBody TourniquetDto tourniquetDto) {
        ApiResponse apiResponse = tourniquetService.editTourniquet(id, tourniquetDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    @GetMapping("/getAll")
    public List<Tourniquet> getAllTourniquet() {
        return tourniquetService.getTourniquetList();
    }

    @GetMapping("/getById/{id}")
    public Tourniquet getByTourniquetId(@PathVariable Integer id) {
        return tourniquetService.getTourniquetById(id);
    }

    @DeleteMapping("/deleteById/{id}")
    public HttpEntity<?> deleteByTourniquetId(@PathVariable Integer id) {
        ApiResponse apiResponse = tourniquetService.deleteTourniquet(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
