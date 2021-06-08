package uz.pdp.online.lesson_5_task_1_hr_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Task;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TaskRepos extends JpaRepository<Task, UUID> {

    Optional<Task> findByEmployeeEmailAndTaskCode(@Email String employeeEmail, String taskCode);

    Set<Task> findAllByEmployeeEmailAndCompletedAndCompletedOnTime(@Email String employeeEmail, boolean completed, boolean completedOnTime);
}
