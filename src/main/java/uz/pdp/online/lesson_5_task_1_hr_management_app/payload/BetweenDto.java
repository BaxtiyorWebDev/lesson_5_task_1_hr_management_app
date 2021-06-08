package uz.pdp.online.lesson_5_task_1_hr_management_app.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class BetweenDto {
    private Integer tourniquetId;
    private Long start;
    private Long end;

}
