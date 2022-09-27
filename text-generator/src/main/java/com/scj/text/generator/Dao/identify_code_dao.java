package com.scj.text.generator.Dao;

import com.scj.text.generator.Entity.identify_code;
import com.scj.text.generator.Entity.user;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface identify_code_dao {
    public void insert_new_identify_code(identify_code new_identify_code);
    public List<identify_code> select_identify_code(@Param("email")String email, @Param("identify_code")String identify_code);
}
