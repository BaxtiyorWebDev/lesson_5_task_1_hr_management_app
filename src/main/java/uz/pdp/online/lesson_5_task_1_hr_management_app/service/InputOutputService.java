package uz.pdp.online.lesson_5_task_1_hr_management_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.InputOutput;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.Tourniquet;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.RoleName;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.BetweenDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.InputOutputDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.InputOutputRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.TourniquetRepos;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InputOutputService {
    @Autowired
    InputOutputRepos inputOutputRepos;
    @Autowired
    TourniquetRepos tourniquetRepos;

    public ApiResponse writeInput(InputOutputDto inputOutputDto) {
        Optional<Tourniquet> optionalTourniquet = tourniquetRepos.findById(inputOutputDto.getTourniquetId());
        if (!optionalTourniquet.isPresent())
            return new ApiResponse("Ushbu id lik turniket MO dan topilmadi",false);

        InputOutput inputOutput = new InputOutput();
        inputOutput.setTourniquet(optionalTourniquet.get());
        inputOutput.setEntered(true);
        inputOutputRepos.save(inputOutput);
        return new ApiResponse("Xush kelibsiz!",true);
    }

    public ApiResponse writeOutput(InputOutputDto inputOutputDto) {
        Optional<Tourniquet> optionalTourniquet = tourniquetRepos.findById(inputOutputDto.getTourniquetId());
        if (!optionalTourniquet.isPresent())
            return new ApiResponse("Ushbu id lik turniket MO dan topilmadi",false);

        InputOutput inputOutput = new InputOutput();
        inputOutput.setTourniquet(optionalTourniquet.get());
        inputOutput.setEntered(false);
        inputOutputRepos.save(inputOutput);
        return new ApiResponse("Oq yo'l!",true);
    }

    public List<InputOutput> getAllInputOutputHistory() {
        List<InputOutput> inputOutputs = inputOutputRepos.findAll();
        return inputOutputs;
    }

    public InputOutput getInputOutputById(UUID id) {
        Optional<InputOutput> optionalInputOutput = inputOutputRepos.findById(id);
        return optionalInputOutput.orElse(null);
    }

    public List<InputOutput> getInputOutputBetweenTime(BetweenDto betweenDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.equals(RoleName.ROLE_EMPLOYEE.name()))
                return null;
        }

        Optional<Tourniquet> optionalTourniquet = tourniquetRepos.findById(betweenDto.getTourniquetId());
        if (!optionalTourniquet.isPresent())
            return null;

        Tourniquet tourniquet = optionalTourniquet.get();
        Timestamp inputTime = new Timestamp(betweenDto.getStart());
        Timestamp outputTime = new Timestamp(betweenDto.getEnd());

        List<InputOutput> allByTourniquetAndCreatedAtBetween = inputOutputRepos.findAllByTourniquetAndCreatedAtBetween(tourniquet, inputTime, outputTime);
        return allByTourniquetAndCreatedAtBetween;
    }
}
