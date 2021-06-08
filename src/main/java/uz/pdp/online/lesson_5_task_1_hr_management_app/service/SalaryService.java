package uz.pdp.online.lesson_5_task_1_hr_management_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Month;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Salary;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.RoleName;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.BetweenDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.SalaryDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.MonthRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.SalaryRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.UserRepos;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {

    @Autowired
    UserRepos userRepos;
    @Autowired
    MonthRepos monthRepos;
    @Autowired
    SalaryRepos salaryRepos;

    public ApiResponse addSalary(SalaryDto salaryDto) {
        Optional<User> optionalUser = userRepos.findById(salaryDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday foydalanuvchi topilmadi", false);

        boolean existsByUserIdAndMonthIdAndYear = salaryRepos.existsByUserIdAndMonthIdAndYear(salaryDto.getUserId(), salaryDto.getMonthId(), salaryDto.getYear());
        if (existsByUserIdAndMonthIdAndYear)
            return new ApiResponse("Ushbu xodim allaqachon maosh olgan", false);

        Optional<Month> optionalMonth = monthRepos.findById(salaryDto.getMonthId());
        if (!optionalMonth.isPresent())
            return new ApiResponse("Maosh qaysi oy uchun kiritilayotgani aniqlanmadi", false);

        Salary salary = new Salary();
        salary.setUser(optionalUser.get());
        salary.setAmount(salaryDto.getAmount());
        salary.setMonth(optionalMonth.get());
        salary.setYear(salaryDto.getYear());
        salaryRepos.save(salary);
        return new ApiResponse(optionalUser.get().getEmail() + " ga " + salary.getAmount() + " miqdorda oylik maosh ajratildi", true);
    }

    public ApiResponse editSalary(UUID id, SalaryDto salaryDto) {
        Optional<Salary> optionalSalary = salaryRepos.findById(id);
        if (!optionalSalary.isPresent())
            return new ApiResponse("Ushbu id lik maosh topilmadi", false);

        Optional<User> optionalUser = userRepos.findById(salaryDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Ushbu id lik xodim topilmadi", false);

        Optional<Month> optionalMonth = monthRepos.findById(salaryDto.getMonthId());
        if (!optionalMonth.isPresent())
            return new ApiResponse("Ushbu maosh qaysi oy uchunligi aniqlanmadi", false);

        boolean existsByUserIdAndMonthIdAndYearAndIdNot = salaryRepos.existsByUserIdAndMonthIdAndYearAndIdNot(salaryDto.getUserId(), salaryDto.getMonthId(), salaryDto.getYear(), id);
        if (existsByUserIdAndMonthIdAndYearAndIdNot)
            return new ApiResponse("Bunday ma'lumot MO da mavjud", false);

        Salary editingSalary = optionalSalary.get();
        editingSalary.setUser(optionalUser.get());
        editingSalary.setAmount(salaryDto.getAmount());
        editingSalary.setMonth(optionalMonth.get());
        editingSalary.setYear(salaryDto.getYear());
        salaryRepos.save(editingSalary);
        return new ApiResponse("Ma'lumot tahrirlandi", true);
    }


    public List<Salary> getAllSalary() {
        List<Salary> salaryList = salaryRepos.findAll();
        return salaryList;
    }

    public Salary getSalaryById(UUID id) {
        Optional<Salary> optionalSalary = salaryRepos.findById(id);
        return optionalSalary.orElse(null);
    }


    public List<Salary> getSalaryByUserId(UUID userId) {
        Optional<User> optionalUser = userRepos.findById(userId);
        if (!optionalUser.isPresent())
            return null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.equals(RoleName.ROLE_EMPLOYEE.name()))
                return null;
        }

        List<Salary> salaryListByUserId = salaryRepos.findAllByUserId(userId);
        return salaryListByUserId;
    }

    public List<Salary> getSalaryBetweenMonth(Integer start, Integer end) {
        Optional<Month> optionalMonth1 = monthRepos.findById(start);
        Optional<Month> optionalMonth2 = monthRepos.findById(end);
        if (!optionalMonth1.isPresent() && !optionalMonth2.isPresent())
            return null;

        List<Salary> allByMonthBetween = salaryRepos.findAllByMonthBetween(optionalMonth1.get(), optionalMonth2.get());
        return allByMonthBetween;
    }


    public ApiResponse deleteSalaryById(UUID id) {
        Optional<Salary> optionalSalary = salaryRepos.findById(id);
        if (!optionalSalary.isPresent())
            return new ApiResponse("Ushbu id lik maosh topilmadi",false);
        salaryRepos.delete(optionalSalary.get());
        return new ApiResponse("maosh o'chirildi",true);
    }


}
