package uz.pdp.online.lesson_5_task_1_hr_management_app.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Tourniquet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private User user;


}
