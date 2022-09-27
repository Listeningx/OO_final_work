package com.scj.text.generator.service;

public interface forget_password {
    public Boolean sendSimpleMail(String from, String to, String subject);
    public int forget_word_identify_code_check(String email,String identify_code);
    public Boolean email_exist_check(String email);
    public void update_user_password(String email,String new_password);
}
