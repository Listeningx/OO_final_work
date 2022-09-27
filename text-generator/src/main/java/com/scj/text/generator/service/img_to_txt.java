package com.scj.text.generator.service;

import com.scj.text.generator.Entity.img_to_txt_commit_record;

import java.util.List;
import java.util.Map;

public interface img_to_txt {
    public void upload_img_to_txt_record(Integer user_id,String commit_image_url,String txt);
    public List<Map<String,Object>> select_upload_img_to_txt_record(Integer user_id);
}
