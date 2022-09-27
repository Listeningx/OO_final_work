package com.scj.text.generator.serviceImpl;

import com.scj.text.generator.Dao.user_dao;
import com.scj.text.generator.Entity.user;
import com.scj.text.generator.service.change_information;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class change_information_lmpl implements change_information {
    @Autowired
    user_dao user;
    @Override
    public void change_password(Integer user_id ,String password)
    {
        user.update_user_password_by_user_id(password, user_id);
    }
    @Override
    public void upload_user_photo(Integer user_id,String user_photo_url)
    {
        user.update_user_photo_url_by_user_id(user_id,user_photo_url);
    }
    @Override
    public user select_user_by_user_id(Integer user_id)
    {

        return user.select_user_by_user_id(user_id);
    }
}
