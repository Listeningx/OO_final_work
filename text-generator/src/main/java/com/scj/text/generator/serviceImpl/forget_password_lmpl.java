package com.scj.text.generator.serviceImpl;

import com.scj.text.generator.Dao.identify_code_dao;
import com.scj.text.generator.Dao.user_dao;
import com.scj.text.generator.Entity.identify_code;
import com.scj.text.generator.service.forget_password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class forget_password_lmpl implements forget_password {
    @Autowired
    private identify_code_dao new_identify_code_dao;

    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Override
    public Boolean sendSimpleMail(String from, String to, String subject) {
        Random a = new Random();
        String text = String.valueOf(a.nextInt(10000));
        Date A = new Date();
        //创建邮件正文
        Context context = new Context();
        context.setVariable("verifyCode", Arrays.asList(text.split("")));

        //将模块引擎内容解析成html字符串
        String emailContent = templateEngine.process("forgetEmailVerificationCode", context);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            //true表示需要创建一个multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("罚写终结者重置密码验证码");
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
    public int forget_word_identify_code_check(String email, String identify_code) {
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
            return flag;
        }
    }

    @Autowired
    user_dao user;

    @Override
    public Boolean email_exist_check(String email) {
        return user.select_user_by_email(email).size() == 0;
    }

    @Override
    public void update_user_password(String email, String new_password) {
        System.out.println(3);
        user.update_user_password(email, new_password);
        System.out.println(4);
    }
}
