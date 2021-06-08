package uz.pdp.online.lesson_5_task_1_hr_management_app.entity;

import lombok.Data;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.State;

import javax.persistence.*;

@Data
@Entity
public class TaskState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    private State state;
}
