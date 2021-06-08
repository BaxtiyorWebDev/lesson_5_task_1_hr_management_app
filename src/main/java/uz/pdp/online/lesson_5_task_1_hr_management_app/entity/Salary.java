package uz.pdp.online.lesson_5_task_1_hr_management_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.template.AbsEntity;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Salary extends AbsEntity {

    private Double amount;

    @ManyToOne
    private User user;

    @ManyToOne
    private Month month;

    @Column(nullable = false)
    private Integer year;


}
