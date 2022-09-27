package com.scj.text.generator.ctrl;

import com.baidu.aip.ocr.AipOcr;
import com.scj.text.generator.Entity.img_to_txt_commit_record;
import com.scj.text.generator.Entity.user;
import com.scj.text.generator.service.change_information;
import com.scj.text.generator.service.img_to_txt;
import com.scj.text.generator.util.JsonChange;
import com.scj.text.generator.util.JwtUitls;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class image_to_txt {

    String path = "/root/images/img_to_txt_img/";
    //String base_picture_url="http://localhost:8090/images/img_to_txt_img/";
    String base_picture_url = "http://101.42.173.97:8090/images/img_to_txt_img/";
    //String path ="D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\img_to_txt_img\\";
    @Autowired
    change_information change_information_service;
    @Autowired
    img_to_txt img_to_txt_service;
    @Autowired
    JwtUitls JWT;
    @PostMapping(value = "/upload_img_to_txt")
    public Map<String, Object> upload_img_to_txt(@RequestParam("file") MultipartFile file, Integer user_id,String jwt) throws IOException, TesseractException {
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

        }catch (Exception e){
            System.out.println("上传失败");
            map.put("success",false);
            map.put("message","上传失败");
            e.printStackTrace();
            return map;
        }
        ITesseract iTesseract = new Tesseract();
        //设置tessdata训练库语言包地址，项目根目录下为默认地址可不设置
        iTesseract.setDatapath("/root/tessdata");

        //默认识别英文
        //如果需要识别英文之外的语种，需要指定识别语种，并且需要将对应的语言包放进项目中
        iTesseract.setLanguage("chi_sim");

        // 指定本地图片
        File img = new File(path+fileName);
        //开始识别时间
        long startTime = System.currentTimeMillis();
        //识别结果
        String ocrResult = iTesseract.doOCR(img);
        // 输出识别结果
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("识别结果: \n" + ocrResult );
        img_to_txt_service.upload_img_to_txt_record(user_id,base_picture_url+fileName,ocrResult);
        map.put("success",true);
        map.put("message","上传成功");
        map.put("picture",base_picture_url+ fileName);
        map.put("txt",ocrResult);
        map.put("jwt",JWT.createToken(user_id,user_now.getUsername()));

        return map;

    }
    @PostMapping(value = "/max_img_to_txt")
    public Map<String, Object> ocr(@RequestParam("file") MultipartFile file, Integer user_id,String jwt) throws Exception {
        // 参数为二进制数组
        System.out.println(1);
        byte[] buf = file.getBytes();
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

        }catch (Exception e){
            System.out.println("上传失败");
            map.put("success",false);
            map.put("message","上传失败");
            e.printStackTrace();
            return map;
        }
        long startTime = System.currentTimeMillis();
        AipOcr client = new AipOcr("26452299", "ruct4BTfDTTM1wFqER6GVYed", "Elko0cqUa6ZWIVe9BWxlGpVrAglMrSDw");
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>(4);
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "true");

        String ocrResult="";
        JSONObject res = client.basicGeneral(buf, options);
        System.out.println(1);
        Map A = JsonChange.json2map(res.toString());
        System.out.println(A);
        //  提取并打印出识别的文字
        try {
            List list = (List) A.get("words_result");
            int len = ((List) A.get("words_result")).size();
            System.out.println(2);
        }catch (Exception E)
        {
            System.out.println(E);
        }
        List list = (List) A.get("words_result");
        int len = ((List) A.get("words_result")).size();
        for(int i=0; i<len; i++) {
            ocrResult =ocrResult + ((Map) list.get(i)).get("words") + "\n";
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        System.out.println("识别结果: \n" + ocrResult );
        img_to_txt_service.upload_img_to_txt_record(user_id,base_picture_url+fileName,ocrResult);
        map.put("success",true);
        map.put("message","上传成功");
        map.put("picture",base_picture_url+ fileName);
        map.put("txt",ocrResult);
        map.put("jwt",JWT.createToken(user_id,user_now.getUsername()));
        return map;
    }
    @PostMapping(value = "/get_img_to_txt_record")
    public Map<String, Object> get_img_to_txt_record(Integer user_id,String jwt)
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
        List<Map<String,Object>> img_to_txt_commit_records = img_to_txt_service.select_upload_img_to_txt_record(user_id);
        if(img_to_txt_commit_records.size()==0)
        {
            map.put("success", false);
            map.put("message", "无提交记录");
        }
        else
        {
            map.put("success", true);
            map.put("message", "获取提交记录成功");
            map.put("commit_records",img_to_txt_commit_records);
            map.put("jwt",JWT.createToken(user_id,user_now.getUsername()));
        }
        return map;
    }

}

