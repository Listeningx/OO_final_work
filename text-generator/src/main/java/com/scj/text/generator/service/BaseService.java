package com.scj.text.generator.service;

import java.util.Map;

public interface BaseService{
    public int user_is_exist(Integer user_id);
    void text(Boolean start,String title, String text, String color, Integer user_id, Integer commit_id,int pageNum,int type,int background_type) throws Exception;
    double get_process(Integer user_id);
    public void set_process(Integer user_id);
}
