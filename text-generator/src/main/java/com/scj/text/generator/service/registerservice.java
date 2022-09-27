package com.scj.text.generator.service;

import com.scj.text.generator.Entity.user;
import org.springframework.stereotype.Component;


public interface registerservice {
    public user select_user_by_username(String username);
    public void register_new_user(user new_user);
    public Boolean sendSimpleMail(String from, String to, String subject, String text);
    public Boolean email_exist_check(String email);
    public int register_identify_code_check(String email,String identify_code);
}
