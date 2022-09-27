package com.scj.text.generator.Dao;

import com.scj.text.generator.Entity.commit_record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface commit_record_dao {
    public void insert_new_commit(commit_record new_commit_record);
    public List<commit_record> select_user_commit_list(Integer user_id);
    public  void delete_by_commit_id(@Param("commit_id")Integer commit_id, @Param("user_id") Integer user_id);
}
