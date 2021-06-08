package uz.pdp.online.lesson_5_task_1_hr_management_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Task extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private TaskState taskState;

    @Email
    private String employeeEmail;

    private Timestamp deadLineTime; //vazifani yakunlanish vaqti

    private String taskCode;

    private boolean completed;

    private boolean completedOnTime;





}
