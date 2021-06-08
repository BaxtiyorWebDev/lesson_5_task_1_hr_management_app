package uz.pdp.online.lesson_5_task_1_hr_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.InputOutput;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Tourniquet;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface InputOutputRepos extends JpaRepository<InputOutput, UUID> {

    List<InputOutput> findAllByTourniquetAndCreatedAtBetween(Tourniquet tourniquet, Timestamp inputTime, Timestamp outputTime);
}
