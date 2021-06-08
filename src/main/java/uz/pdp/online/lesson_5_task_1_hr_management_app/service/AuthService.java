package uz.pdp.online.lesson_5_task_1_hr_management_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.User;
import uz.pdp.online.lesson_5_task_1_hr_management_app.entity.enums.RoleName;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.ApiResponse;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.LoginDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.payload.RegisterDto;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.RoleRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.UserRepos;
import uz.pdp.online.lesson_5_task_1_hr_management_app.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;


@Service
public class AuthService implements UserDetailsService {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepos userRepos;
    @Autowired
    RoleRepos roleRepos;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (Exception e) {
            return new ApiResponse("Parol yoki login xato", false);
        }
    }

    public ApiResponse register(RegisterDto registerDto) {
        boolean existsByEmail = userRepos.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Bunday foydalanuvchi MO da mavjud", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        for (GrantedAuthority authority : user.getAuthorities()) {
            if (authority.getAuthority().equals(RoleName.ROLE_DIRECTOR.name()) || authority.getAuthority().equals(RoleName.ROLE_HR_MANAGER.name())) {
                User addingUser = new User();
                addingUser.setFullName(registerDto.getFullName());
                addingUser.setEmail(registerDto.getEmail());

                addingUser.setRoles(Collections.singleton(roleRepos.getById(registerDto.getRoleId())));
                addingUser.setPassword(passwordEncoder.encode("1234"));
                addingUser.setEmailCode(UUID.randomUUID().toString());
                userRepos.save(addingUser);

                sendEmail(registerDto.getEmail(), user.getEmailCode());
                return new ApiResponse("Foydalanuvchi tizimdan ro'yxatdan o'tdi, e-pochtasiga tasdiqlash uchun xabar yuborildi", true);
            } else {
                return new ApiResponse("Xodim rolidagi foydalanuvchi tizimga hech kimni qo'sha olmaydi", false);
            }
        }
        return new ApiResponse("Xatolik!",false);
    }

    public boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("CherryCor@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Tizimga kirishni tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?email=" + sendingEmail + "&emailCode=" + emailCode + "'>Ushbu linkka o'ting</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String email, String emailCode, LoginDto loginDto) {
        try {
            Optional<User> optionalUser = userRepos.findByEmailAndEmailCode(email, emailCode);
            if (!optionalUser.isPresent())
                return new ApiResponse("Bunday foydalanuvchi mavjud emas", false);

            User user = optionalUser.get();
            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(loginDto.getPassword()));
            user.setEmailCode(null);
            userRepos.save(user);
            return new ApiResponse("Ro'yxatdan o'tdingiz", true);
        } catch (UsernameNotFoundException e) {
            return new ApiResponse("Siz allaqachon ro'yxatdan o'tgansiz", false);
        }

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepos.findByEmail(username);
        if (optionalUser.isPresent())
            return optionalUser.get();
        throw new UsernameNotFoundException(username + " topilmadi");
    }
}
