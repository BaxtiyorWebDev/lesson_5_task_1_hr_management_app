package uz.pdp.online.lesson_5_task_1_hr_management_app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class InputOutput extends AbsEntity {

    @ManyToOne
    private Tourniquet tourniquet;

    @Column(nullable = false)
    private boolean entered;
}
