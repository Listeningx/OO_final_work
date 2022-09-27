package com.scj.text.generator.Dao;

import com.scj.text.generator.Entity.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface user_dao {
    public user select_user_by_username(String username);
    public void register_new_user(user new_user);
    public List<user> select_user_by_email(String email);
    public void update_user_password(@Param("email") String email,@Param("new_password") String new_password);
    public void update_user_password_by_user_id(@Param("password")String password,@Param("user_id")Integer user_id);
    public void update_user_photo_url_by_user_id(@Param("user_id")Integer user_id,@Param("user_photo_url")String user_photo_url);
    public user select_user_by_user_id(Integer user_id);
}
