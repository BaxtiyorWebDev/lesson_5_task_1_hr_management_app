package uz.pdp.online.lesson_5_task_1_hr_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepos extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);
}
