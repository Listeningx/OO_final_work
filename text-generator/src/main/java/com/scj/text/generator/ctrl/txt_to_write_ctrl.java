package com.scj.text.generator.ctrl;
import com.scj.text.generator.Dao.commit_record_dao;

import com.scj.text.generator.Entity.commit_record;
import com.scj.text.generator.Entity.user;
import com.scj.text.generator.service.*;
import com.scj.text.generator.util.JwtUitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class txt_to_write_ctrl {
    @Autowired
    BaseService baseService;
    @Autowired
    commit_record_dao new_commit_record_dao;
    @Autowired
    get_commit_list new_get_commit_list;
    @Autowired
    JwtUitls JWT;
    @Autowired
    change_information new_change_information;
    @PostMapping(value = "/text")
    public Map<String, Object> test(String title, String text, String color, Integer user_id,String jwt,int type,int background_type) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if(baseService.user_is_exist(user_id)==0)
        {
            map.put("success",false);
            map.put("message","用户不存在");
            return map;
        }
        if(JWT.verify(jwt,user_id)==2)
        {
            map.put("success", false);
            map.put("message", "长时间未操作，请重新登录");
            return map;
        }
        else if(JWT.verify(jwt,user_id)==0)
        {
            map.put("success", false);
            map.put("message", "用户验证失败");
            return map;
        }
        try {
            Date A = new Date();//表示提交这个记录时的时间。
            commit_record new_commit_record = new commit_record(user_id, A, title);
            new_commit_record_dao.insert_new_commit(new_commit_record);//把提交记录插入数据库
            System.out.println(new_commit_record.getCommit_id());
            Integer new_commit_record_id = new_commit_record.getCommit_id();
            System.out.println(4);

            baseService.text(true, title, text, color, user_id, new_commit_record_id,0,type,background_type);

            System.out.println(new_get_commit_list.get_image_urls_list(new_commit_record_id));
            map.put("success",true);
            map.put("message","上传成功");
            map.put("image_url",new_get_commit_list.get_image_urls_list(new_commit_record_id));
            map.put("jwt",JWT.createToken(user_id,new_change_information.select_user_by_user_id(user_id).getUsername()));
            baseService.set_process(user_id);
            return map;
        }catch (Exception e)
        {
            System.out.println(e);
            map.put("success",false);
            map.put("message","上传失败");
            return map;
        }
    }



    @PostMapping(value = "/get_commit_list")
    public Map<String, Object> get_commit_list(Integer user_id,String jwt) {
        System.out.println(0);
        System.out.println(1);
        Map<String, Object> map = new HashMap<>();

        if(baseService.user_is_exist(user_id)==0)
        {
            map.put("success",false);
            map.put("message","用户不存在");
        }
        if(JWT.verify(jwt,user_id)==2)
        {
            map.put("success", false);
            map.put("message", "长时间未操作，请重新登录");
            return map;
        }
        else if(JWT.verify(jwt,user_id)==0)
        {
            map.put("success", false);
            map.put("message", "用户验证失败");
            return map;
        }
        try {
            map.put("success",true);
            map.put("message","获取成功");
            map.put("commit_list",new_get_commit_list.get_commit_list_by_user_id(user_id));
            map.put("jwt",JWT.createToken(user_id,new_change_information.select_user_by_user_id(user_id).getUsername()));
            System.out.println(3);
            return map;
        }catch (Exception e)
        {
            map.put("success",false);
            map.put("message","获取失败");
            return map;
        }
    }

    @PostMapping(value = "/get_image_urls_list")
    public Map<String, Object> get_image_urls_list(Integer commit_id,Integer user_id,String jwt) {
        Map<String, Object> map = new HashMap<>();
        if(baseService.user_is_exist(user_id)==0)
        {
            map.put("success",false);
            map.put("message","用户不存在");
        }
        if(JWT.verify(jwt,user_id)==2)
        {
            map.put("success", false);
            map.put("message", "长时间未操作，请重新登录");
            return map;
        }
        else if(JWT.verify(jwt,user_id)==0)
        {
            map.put("success", false);
            map.put("message", "用户验证失败");
            return map;
        }
        try {
            List<Map<String,Object>> a = new_get_commit_list.get_image_urls_list(commit_id);
            if (a.size()==0) {
                map.put("success", false);
                map.put("message", "无图像");
                return map;
            }
            map.put("success",true);
            map.put("message","获取图像成功");
            map.put("jwt",JWT.createToken(user_id,new_change_information.select_user_by_user_id(user_id).getUsername()));
            map.put("image_urls",a);
            return map;
        }
        catch (Exception e)
        {
            map.put("success",false);
            map.put("message","获取图像失败");
            return map;
        }
    }
    @PostMapping(value = "/get_img_to_txt_process")
    public Map<String, Object> get_img_to_txt_process(Integer user_id)
    {
        Map<String, Object> map = new HashMap<>();
        int process = (int) baseService.get_process(user_id);
        map.put("process",process);
        return map;
    }
    @PostMapping(value = "/delete_img_to_txt_record")
    public Map<String, Object> delete_img_to_txt_record(Integer commit_id,Integer user_id,String jwt)
    {
        Map<String, Object> map = new HashMap<>();
        if(baseService.user_is_exist(user_id)==0)
        {
            map.put("success",false);
            map.put("message","用户不存在");
        }
        if(JWT.verify(jwt,user_id)==2)
        {
            map.put("success", false);
            map.put("message", "长时间未操作，请重新登录");
            return map;
        }
        else if(JWT.verify(jwt,user_id)==0)
        {
            map.put("success", false);
            map.put("message", "用户验证失败");
            return map;
        }
        try {
            new_get_commit_list.delete_commit(commit_id,user_id);
        }catch (Exception e)
        {
            System.out.println(e);
            map.put("success", false);
            map.put("message", "删除失败");
            return map;
        }
        map.put("success", true);
        map.put("jwt",JWT.createToken(user_id,new_change_information.select_user_by_user_id(user_id).getUsername()));
        map.put("message", "删除成功");
        return map;

    }
}
