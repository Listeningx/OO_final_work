package com.scj.text.generator.serviceImpl;

import com.scj.text.generator.Dao.commit_images_dao;
import com.scj.text.generator.Dao.commit_record_dao;
import com.scj.text.generator.Entity.commit_images;
import com.scj.text.generator.Entity.commit_record;
import com.scj.text.generator.service.get_commit_list;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class get_commit_list_lmpl implements get_commit_list {
    @Autowired
    commit_record_dao new_commit_record_dao;
    @Override
    public List<Map<String,Object>> get_commit_list_by_user_id(Integer user_id)
    {
        List<Map<String,Object>> a = new ArrayList<>();

        System.out.println(1);
        List<commit_record> user_commit_list = new_commit_record_dao.select_user_commit_list(user_id);
        System.out.println(user_commit_list.get(0).getCommit_id());
        for (int i = user_commit_list.size()-1;i>=0;i--) {
            commit_record commit_record = user_commit_list.get(i);
            Map<String, Object> b = new HashMap<>();
            b.put("commit_id", commit_record.getCommit_id());
            b.put("title", commit_record.getTitle());
            b.put("commit_time", commit_record.getCommit_time());
            a.add(b);
        }
        System.out.println(a);
        return a;
    }
    @Autowired
    commit_images_dao new_commit_images_dao;
    @Override
    public List<Map<String,Object>> get_image_urls_list(Integer commit_id)
    {
        List<Map<String,Object>> a = new ArrayList<>();
        List<commit_images> commit_images_list = new_commit_images_dao.select_commit_images_url(commit_id);
        int i;
        System.out.println(1);
        System.out.println(commit_images_list.size());
        for(i=0;i<commit_images_list.size();i++)
        {
            Map<String,Object> b = new HashMap<>();
            b.put("picture_num",i);
            b.put("picture_url",commit_images_list.get(i).getImage_url());
            a.add(b);
        }
        return a;
    }
    @Override
    public void delete_commit(Integer commit_id,Integer user_id)
    {
        System.out.println(commit_id);
        System.out.println(user_id);
        new_commit_record_dao.delete_by_commit_id(commit_id,user_id);

    }
}
