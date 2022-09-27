package com.scj.text.generator.serviceImpl;

import com.scj.text.generator.Dao.identify_code_dao;
import com.scj.text.generator.Dao.user_dao;
import com.scj.text.generator.Entity.identify_code;
import com.scj.text.generator.Entity.user;
import com.scj.text.generator.service.registerservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class registerservicelmpl implements registerservice {
    @Autowired
    private user_dao user;
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    public user select_user_by_username(String username) {
        return user.select_user_by_username(username);
    }

    @Override
    public void register_new_user(user new_user) {
        user.register_new_user(new_user);
    }

    @Autowired
    private identify_code_dao new_identify_code_dao;

    @Autowired
    TemplateEngine templateEngine;


    @Override
    public Boolean sendSimpleMail(String from, String to, String subject, String text) {
        Date A = new Date();
        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(text.split("")));

        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("EmailVerificationCode", context);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("罚写终结者注册验证码");
            helper.setText(emailContent, true);
            javaMailSender.send(message);
            identify_code register_identify_code = new identify_code(text, to, 0, A);
            new_identify_code_dao.insert_new_identify_code(register_identify_code);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    @Override
    public Boolean email_exist_check(String email) {
        if (user.select_user_by_email(email).size() != 0) {
            return false;
        } else
            return true;
    }

    @Override
    public int register_identify_code_check(String email, String identify_code) {
        List<identify_code> identify_list = new_identify_code_dao.select_identify_code(email, identify_code);
        if (identify_list.size() == 0)
            return 0;
        else {
            int flag = 1;
            Date now = new Date();
            for (com.scj.text.generator.Entity.identify_code identifyCode : identify_list) {
                if ((now.getTime() - identifyCode.getDate().getTime()) / 1000 <= 300) {
                    flag = 2;
                    break;
                }
            }
            System.out.println(flag);
            return flag;
        }
    }

}
