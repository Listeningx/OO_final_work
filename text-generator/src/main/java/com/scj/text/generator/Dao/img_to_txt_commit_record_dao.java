package com.scj.text.generator.Dao;

import com.scj.text.generator.Entity.img_to_txt_commit_record;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface img_to_txt_commit_record_dao {
    public void insert_img_to_txt_commit(img_to_txt_commit_record new_img_to_txt_commit_record);
    public List<img_to_txt_commit_record> select_img_to_txt_commit_record(Integer user_id);
}
