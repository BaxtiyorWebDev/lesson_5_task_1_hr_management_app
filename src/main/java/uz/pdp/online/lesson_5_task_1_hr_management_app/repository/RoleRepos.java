package uz.pdp.online.lesson_5_task_1_hr_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Role;

import java.util.Optional;

public interface RoleRepos extends JpaRepository<Role, Integer> {
}
