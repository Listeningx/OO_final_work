package com.scj.text.generator.service;

import com.scj.text.generator.Entity.user;

public interface change_information {
    public void change_password(Integer user_id,String password);
    public void upload_user_photo(Integer user_id,String user_photo_url);
    public user select_user_by_user_id(Integer user_id);
}
