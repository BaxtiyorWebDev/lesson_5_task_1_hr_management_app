package uz.pdp.online.lesson_5_task_1_hr_management_app.payload;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {

    @NotNull
    @Length(min = 2,max = 40)
    private String fullName;

    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Integer roleId;
}
