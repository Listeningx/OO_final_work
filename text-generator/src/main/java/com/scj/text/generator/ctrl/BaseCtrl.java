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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
@author 罚写终结者团队
@version jdk1.8.0
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class BaseCtrl {
    @Autowired
    JwtUitls JWT;

    @Autowired
    private registerservice register;
    /**
     * 求输入两个参数范围以内整数的和
     * @param re_map 里只有一个参数，email，表示用户注册使用的邮箱
     * @return 当邮箱位数大于30时，返回邮箱过长,如果邮箱格式不正确，返回邮箱格式不正确，如果邮箱
     */
    @PostMapping("/register/identify_code_send")
    public Map<String, Object> register_code_send(@RequestParam Map<String, String> re_map) {
        String email = re_map.get("email");
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        //\w+@(\w+.)+[a-z]{2,3}
        Matcher matcher = pattern.matcher(email);
        Map<String, Object> map = new HashMap<>();
        if (email.length() > 30) {
            map.put("success", false);
            map.put("message", "邮箱过长");
            return map;
        }
        if (!matcher.matches()) {
            map.put("success", false);
            map.put("message", "邮箱格式不正确！");
            return map;
        }
        if (!register.email_exist_check(email)) {
            map.put("success", false);
            map.put("message", "邮箱已被注册！");
            return map;
        }
        try {
            Random a = new Random();
            String identify_code = String.valueOf(a.nextInt(10000));
            Boolean flag = register.sendSimpleMail("853048903@qq.com", email, "罚写终结者注册验证码", identify_code);
            if (flag) {
                map.put("success", true);
                map.put("message", "注册验证码发送成功！");
            } else {
                map.put("success", false);
                map.put("message", "注册验证码发送失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "注册验证码发送失败！");
        }
        return map;
    }
    //String default_user_photo_url = "http://localhost:8090/images/user_photo/common.jpg";
    String default_user_photo_url = "http://101.42.173.97:8090/images/user_photo/common.jpg";
    @PostMapping("/register/check_identify_code")
    public Map<String, Object> register(@RequestParam Map<String, String> re_map) {
        String email = re_map.get("email");
        String identify_code = re_map.get("identify_code");
        String username = re_map.get("username");
        String password1 = re_map.get("password1");
        String password2 = re_map.get("password2");
        Map<String, Object> map = new HashMap<>();
        if (email.length() > 30) {
            map.put("success", false);
            map.put("message", "邮箱过长");
            return map;
        }
        if (identify_code.length() > 4) {
            map.put("success", false);
            map.put("message", "验证码过长");
            return map;
        }
        if (password1.length() > 20 || password2.length() > 20) {
            map.put("success", false);
            map.put("message", "密码过长");
            return map;
        }
        if(username.length()>20)
        {
            map.put("success", false);
            map.put("message", "用户名过长");
            return map;
        }
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        //\w+@(\w+.)+[a-z]{2,3}
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            map.put("success", false);
            map.put("message", "邮箱格式不正确！");
            return map;
        }
        if (!register.email_exist_check(email)) {
            map.put("success", false);
            map.put("message", "邮箱已被注册！");
            return map;
        }
        try {
            int flag = register.register_identify_code_check(email, identify_code);
            System.out.println(flag);
            if (flag == 0) {
                map.put("success", false);
                map.put("message", "验证码不正确！");
                return map;
            } else if (flag == 1) {
                map.put("success", false);
                map.put("message", "验证码已失效！");
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "用户注册失败！");
        }

        try {
            user user1 = register.select_user_by_username(username);
            if (user1 != null) {
                map.put("success", false);
                map.put("message", "用户已被注册！");
            } else if (!password1.equals(password2)) {
                map.put("success", false);
                map.put("message", "两次密码不一致！");
            } else {
                user new_user = new user(username, password1, email,default_user_photo_url);
                register.register_new_user(new_user);
                map.put("success", true);
                map.put("message", "用户注册成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "用户注册失败！");
        }
        return map;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam Map<String, String> re_map) {
        String username = re_map.get("username");
        String password = re_map.get("password");
        Map<String, Object> map = new HashMap<>();
        if (password.length() > 20 ) {
            map.put("success", false);
            map.put("message", "密码过长");
            return map;
        }
        if(username.length()>20)
        {
            map.put("success", false);
            map.put("message", "用户名过长");
            return map;
        }
        try {
            user user1 = register.select_user_by_username(username);
            if (user1 != null) {
                if (user1.getPassword().equals(password)) {
                    map.put("success", true);
                    map.put("message", "用户登录成功！");
                    map.put("user_id",user1.getUser_id());
                    map.put("username",user1.getUsername());
                    map.put("jwt",JWT.createToken(user1.getUser_id(),username));
                    map.put("user_photo",user1.getUser_photo_url());
                } else {
                    map.put("success", false);
                    map.put("message", "密码错误！");
                }
            } else {
                map.put("success", false);
                map.put("message", "用户不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("message", "用户登录失败！");
        }
        return map;
    }

    @Autowired
    forget_password forget_password_service;

    @PostMapping("/forget_password/send_identify_code")
    public Map<String, Object> send_forget_password_identify_code(@RequestParam Map<String, String> re_map) {
        Map<String, Object> map = new HashMap<>();
        String email = re_map.get("email");
        if (email.length() > 30) {
            map.put("success", false);
            map.put("message", "邮箱过长");
            return map;
        }
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        //\w+@(\w+.)+[a-z]{2,3}
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            map.put("success", false);
            map.put("message", "邮箱格式不正确！");
            return map;
        }
        try {
            if (forget_password_service.email_exist_check(email)) {
                map.put("success", false);
                map.put("message", "此邮箱不存在");
                return map;
            }
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "验证码发送失败");
            return map;
        }
        try {
            boolean flag;
            flag = forget_password_service.sendSimpleMail("853048903@qq.com", email, "忘记密码验证码");
            if (!flag) {
                map.put("success", false);
                map.put("message", "验证码发送失败");
                return map;
            }
        } catch (Exception E) {
            map.put("success", false);
            map.put("message", "验证码发送失败");
            return map;
        }
        map.put("success", true);
        map.put("message", "验证码发送成功");
        return map;
    }

    @PostMapping("/forget_password/checkout_identify_code")
    public Map<String, Object> checkout_forget_password_identify_code(@RequestParam Map<String, String> re_map) {
        String email = re_map.get("email");
        String identify_code = re_map.get("identify_code");
        String password1 = re_map.get("password1");
        String password2 = re_map.get("password2");
        Map<String, Object> map = new HashMap<>();
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        //\w+@(\w+.)+[a-z]{2,3}
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            map.put("success", false);
            map.put("message", "邮箱格式不正确！");
            return map;
        }
        if (email.length() > 30) {
            map.put("success", false);
            map.put("message", "邮箱过长");
            return map;
        }
        if (identify_code.length() > 4) {
            map.put("success", false);
            map.put("message", "验证码过长");
            return map;
        }
        if (password1.length() > 20 || password2.length() > 20) {
            map.put("success", false);
            map.put("message", "新密码过长");
            return map;
        }
        if (!password1.equals(password2)) {
            map.put("success", false);
            map.put("message", "两次密码不一致");
            return map;
        }
        try {
            if (forget_password_service.email_exist_check(email)) {
                map.put("success", false);
                map.put("message", "此邮箱不存在");
                return map;
            }
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "重置密码失败");
            System.out.println(1);
            return map;
        }
        try {
            int flag = forget_password_service.forget_word_identify_code_check(email, identify_code);
            if (flag == 0) {
                map.put("success", false);
                map.put("message", "验证码错误");
                return map;
            } else if (flag == 1) {
                map.put("success", false);
                map.put("message", "验证码失效");
                return map;
            }
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "验证失败");
            return map;
        }
        try {
            forget_password_service.update_user_password(email, password1);
        } catch (Exception e) {
            map.put("success", false);
            map.put("message", "重置密码失败");
            System.out.println(2);
            return map;
        }
        map.put("success", true);
        map.put("message", "重置密码成功");
        return map;
    }
    @Autowired
    change_information change_information_service;
    @PostMapping("/change_password")
    public Map<String, Object> change_password(@RequestParam Map<String, String> re_map)
    {
        Integer user_id = Integer.parseInt(re_map.get("user_id"));
        String jwt =re_map.get("jwt");
        String password = re_map.get("new_password");
        Map<String, Object> map = new HashMap<>();
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
            change_information_service.change_password(user_id, password);
        }
        catch (Exception e)
        {
            System.out.println(e);
            map.put("success", false);
            map.put("message", "重置密码失败");
            return map;
        }
        map.put("success", true);
        map.put("message", "重置密码成功");
        map.put("jwt",JWT.createToken(user_id,change_information_service.select_user_by_user_id(user_id).getUsername()));
        return map;
    }
    String path = "/root/images/user_photo/";
    //String base_picture_url="http://localhost:8090/images/user_photo/";
    String base_picture_url="http://101.42.173.97:8090/images/user_photo/";
    //String path ="D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\user_photo\\";
    @PostMapping("/uploadPicture")
    public Map<String, Object> uploadPicture(@RequestParam("file") MultipartFile file, Integer user_id,String jwt) throws IOException
    {
        Map<String, Object> map = new HashMap<>();
        user user_now = change_information_service.select_user_by_user_id(user_id);
        if(user_now==null)
        {
            map.put("success", false);
            map.put("message", "用户不存在");
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
        //String base_picture_url = "http://localhost:8080/image/";
        //
        //获取文件在服务器的储存位置
        File filePath = new File(path);
        System.out.println("文件的保存路径"+path);

        if(!filePath.exists() && !filePath.isDirectory()){
            System.out.println("目录不存在，创建目录" + filePath);
            filePath.mkdir();
        }

        //获取原始文件名称（包括格式）
        String originalFileName = file.getOriginalFilename();
        System.out.println("原始文件名称" + originalFileName);

        //获取文件类型，以最后一个‘.’为标识
        assert originalFileName != null;
        String type = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        System.out.println("文件类型" + type);
        if(!(type.equals("png")||type.equals("jpg")))
        {
            map.put("success",false);
            map.put("message","文件格式有误");
            return map;
        }

        //获取文件名称（不包含格式）
        String name = originalFileName.substring(0,originalFileName.lastIndexOf("."));

        //设置文件新名称：当前事件+文件名称（不包含格式）
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(d);
        String fileName = user_id+date + name + "." +type;
        System.out.println("新文件名称" +fileName);

        //在指定路径下创建文件
        File targetFile = new File(path,fileName);


        //将文件保存到服务器指定位置
        try{
            file.transferTo(targetFile);
            System.out.println("上传成功");
            change_information_service.upload_user_photo(user_id,base_picture_url+fileName);
        }catch (Exception e){
            System.out.println("上传失败");
            map.put("success",false);
            map.put("message","上传失败");
            e.printStackTrace();
            return map;
        }

        map.put("success",true);
        map.put("message","上传成功");
        map.put("jwt",JWT.createToken(user_id,user_now.getUsername()));
        map.put("picture",base_picture_url+ fileName);

        return map;

    }
}
