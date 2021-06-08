package uz.pdp.online.lesson_5_task_1_hr_management_app.service.emailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.online.lesson_5_task_1_hr_management_app.repository.TaskRepos;

@Service
public class CustomEmailService {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    TaskRepos taskRepos;

    public boolean sendEmail(String sendingEmail, String taskCode, String linkName, String taskName, String taskDescription) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Cherry@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(taskName);
            mailMessage.setText(taskName);
            mailMessage.setText("<a href='http://localhost:8080/api/task/" + linkName + "?taskCode=" + taskCode + "&email=" + sendingEmail + "'>Ushbu havolaga o'ting</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendEmail(String sendingEmail, String taskName, String remember) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Cherry@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(taskName+". Egasi: "+remember);
            mailMessage.setText(taskName);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean sendEmail(String sendingEmail, String fromEmployee, String taskName, String taskState) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("Cherry@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject(taskName+". Vazifa holati o'zgardi: "+taskState);
            mailMessage.setText(fromEmployee+" dan " + sendingEmail +" ga vazifa yuborildi");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
