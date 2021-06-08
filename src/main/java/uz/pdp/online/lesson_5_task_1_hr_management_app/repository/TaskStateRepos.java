package uz.pdp.online.lesson_5_task_1_hr_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.TaskState;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.State;


public interface TaskStateRepos extends JpaRepository<TaskState, Integer> {
    TaskState findByState(State state);
}
