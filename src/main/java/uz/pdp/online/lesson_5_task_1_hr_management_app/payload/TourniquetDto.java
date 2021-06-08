package uz.pdp.online.lesson_5_task_1_hr_management_app.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class TourniquetDto {

    @NotNull
    private UUID userId;
}
