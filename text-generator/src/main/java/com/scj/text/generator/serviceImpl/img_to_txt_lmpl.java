package com.scj.text.generator.serviceImpl;

import com.scj.text.generator.Dao.img_to_txt_commit_record_dao;
import com.scj.text.generator.Entity.img_to_txt_commit_record;
import com.scj.text.generator.service.img_to_txt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class img_to_txt_lmpl implements img_to_txt {
    @Autowired
    img_to_txt_commit_record_dao new_img_to_txt_commit_record_dao;
    @Override
    public void upload_img_to_txt_record(Integer user_id,String commit_image_url,String txt)
    {
        Date A = new Date();
        img_to_txt_commit_record new_img_to_txt_commit_record = new img_to_txt_commit_record(user_id,commit_image_url,txt,A);
        new_img_to_txt_commit_record_dao.insert_img_to_txt_commit(new_img_to_txt_commit_record);
    }
    @Override
    public List<Map<String,Object>> select_upload_img_to_txt_record(Integer user_id)
    {
        List<Map<String,Object>> A = new ArrayList<>();
        List<img_to_txt_commit_record> user_img_to_txt_commit_record_list = new_img_to_txt_commit_record_dao.select_img_to_txt_commit_record(user_id);
        System.out.println(1);
        if(user_img_to_txt_commit_record_list.size()==0)
            return A;
        else
        {
            for (com.scj.text.generator.Entity.img_to_txt_commit_record img_to_txt_commit_record : user_img_to_txt_commit_record_list) {
                Map<String, Object> b = new HashMap<>();
                b.put("commit_id", img_to_txt_commit_record.getImg_to_txt_commit_record_id());
                b.put("image_url", img_to_txt_commit_record.getCommit_image_url());
                b.put("txt", img_to_txt_commit_record.getTxt());
                b.put("commit_time", img_to_txt_commit_record.getCommit_time());
                A.add(b);
            }
        }
        return A;
    }
}
