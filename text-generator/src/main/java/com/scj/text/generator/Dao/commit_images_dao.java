package com.scj.text.generator.Dao;

import com.scj.text.generator.Entity.commit_images;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface commit_images_dao {
    public void insert_commit_image_url(commit_images new_commit_image);
    public List<commit_images> select_commit_images_url(Integer commit_id);
}
