package uz.pdp.online.lesson_5_task_1_hr_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Month;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Salary;

import java.util.List;
import java.util.UUID;

public interface SalaryRepos extends JpaRepository<Salary, UUID> {

    boolean existsByUserIdAndMonthIdAndYear(UUID user_id, Integer month_id, Integer year);

    boolean existsByUserIdAndMonthIdAndYearAndIdNot(UUID user_id, Integer month_id, Integer year, UUID id);

    List<Salary> findAllByUserId(UUID user_id);

    List<Salary> findAllByMonthBetween(Month month1, Month month2);
}
