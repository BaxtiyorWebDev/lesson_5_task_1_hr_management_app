package uz.pdp.online.lesson_5_task_1_hr_management_app.payload;

import lombok.Data;

@Data
public class QueryDto {
    private String email;
    private boolean completed;
    private boolean completedOnTime;
}
