package uz.pdp.online.lesson_5_task_1_hr_management_app.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class TaskDto {

    @Size(min = 2, max = 20)
    private String name;

    @Size(min = 3)
    private String description;

    @NotNull
    private Integer taskStateId;

    @NotNull
    @Email
    private String employeeEmail;


    private Long deadLineDate;
}
