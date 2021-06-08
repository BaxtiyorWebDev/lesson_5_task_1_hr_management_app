package uz.pdp.online.lesson_5_task_1_hr_management_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Tourniquet;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.TourniquetDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.TourniquetRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.UserRepos;

import java.util.List;
import java.util.Optional;

@Service
public class TourniquetService {

    @Autowired
    TourniquetRepos tourniquetRepos;
    @Autowired
    UserRepos userRepos;

    public ApiResponse addTourniquet(TourniquetDto tourniquetDto) {
        Optional<User> optionalUser = userRepos.findById(tourniquetDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Ushbu id lik foydalanuvchi mavjud emas", false);
        User user = optionalUser.get();

        boolean existsByUser = tourniquetRepos.existsByUser(user);
        if (existsByUser)
            return new ApiResponse("Bunday foydalanuvchi MO mavjud", false);

        Tourniquet tourniquet = new Tourniquet();
        tourniquet.setUser(user);
        tourniquetRepos.save(tourniquet);
        return new ApiResponse("Turniket saqlandi", true);
    }

    public List<Tourniquet> getTourniquetList() {
        return tourniquetRepos.findAll();
    }

    public Tourniquet getTourniquetById(Integer id) {
        Optional<Tourniquet> optionalTourniquet = tourniquetRepos.findById(id);
        return optionalTourniquet.orElse(null);
    }

    public ApiResponse editTourniquet(Integer id, TourniquetDto tourniquetDto) {
        boolean existsByUserIdAndIdNot = tourniquetRepos.existsByUserIdAndIdNot(tourniquetDto.getUserId(), id);
        if (!existsByUserIdAndIdNot)
            return new ApiResponse("Bunday foydalanuvchi MO da mavjud", false);

        Optional<Tourniquet> optionalTourniquet = tourniquetRepos.findById(id);
        if (!optionalTourniquet.isPresent())
            return new ApiResponse("Ushbu id lik turniket topilmadi", false);

        Tourniquet editingTourniquet = optionalTourniquet.get();
        Optional<User> optionalUser = tourniquetRepos.findByUserId(tourniquetDto.getUserId());
        if (!optionalUser.isPresent())
            return new ApiResponse("Ushbu id lik foydalanuvchi topilmadi", false);
        editingTourniquet.setUser(optionalUser.get());
        tourniquetRepos.save(editingTourniquet);
        return new ApiResponse("Turniket tahrirlandi", true);
    }

    public ApiResponse deleteTourniquet(Integer id) {
        Optional<Tourniquet> optionalTourniquet = tourniquetRepos.findById(id);
        if (!optionalTourniquet.isPresent())
            return new ApiResponse("Bunday turniket MO dan topilmadi", false);
        tourniquetRepos.delete(optionalTourniquet.get());
        return new ApiResponse("id si "+id+" bo'lgan turniket MO dan o'chirildi", true);
    }
}
