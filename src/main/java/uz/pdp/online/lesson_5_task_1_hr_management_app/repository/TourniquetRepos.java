package uz.pdp.online.lesson_5_task_1_hr_management_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Tourniquet;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface TourniquetRepos extends JpaRepository<Tourniquet, Integer> {

    Optional<User> findByUserId(UUID user_id);

    boolean existsByUser(User user);

    boolean existsByUserIdAndIdNot(UUID user_id, Integer id);
}
