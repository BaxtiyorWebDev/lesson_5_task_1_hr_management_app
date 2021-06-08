package uz.pdp.online.lesson_5_task_1_hr_management_app.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Month;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Company;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Role;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.TaskState;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.MonthName;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.RoleName;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.State;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.*;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepos roleRepos;
    @Autowired
    MonthRepos monthRepos;
    @Autowired
    TaskStateRepos taskStateRepos;
    @Autowired
    UserRepos userRepos;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CompanyRepos companyRepos;

    @Value("${spring.sql.init.enabled}")
    private String mode;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("true")) {
            RoleName[] roleNames = RoleName.values();
            for (RoleName value : roleNames) {
                Role role = new Role();
                role.setRoleName(value);
                roleRepos.save(role);
            }

            State[] states = State.values();
            for (State state : states) {
                TaskState taskState = new TaskState();
                taskState.setState(state);
                taskStateRepos.save(taskState);
            }

            MonthName[] monthNames = MonthName.values();
            for (MonthName monthName : monthNames) {
                Month month = new Month();
                month.setMonthName(monthName);
                monthRepos.save(month);
            }

            User user = new User();
            user.setFullName("BAXTIYOR");
            user.setEmail("baxtiyor@gmail.com");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setEnabled(true);
            user.setRoles(Collections.singleton(roleRepos.getById(1)));
            userRepos.save(user);

            Company company = new Company();
            company.setName("Cherry");
            company.setDirector(user.getFullName());
            company.setAddress("Tashkent region");
            companyRepos.save(company);
        }
    }
}
