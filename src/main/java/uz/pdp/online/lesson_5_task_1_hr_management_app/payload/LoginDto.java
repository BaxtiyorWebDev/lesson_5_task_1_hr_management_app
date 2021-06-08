package uz.pdp.online.lesson_5_task_1_hr_management_app.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @Email
    @NotNull
    private String username;

    @NotNull
    private String password;
}
