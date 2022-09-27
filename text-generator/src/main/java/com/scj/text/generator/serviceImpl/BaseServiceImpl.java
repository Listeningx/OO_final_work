package com.scj.text.generator.serviceImpl;

import com.scj.text.generator.Dao.commit_images_dao;
import com.scj.text.generator.Dao.user_dao;
import com.scj.text.generator.Entity.commit_images;
import com.scj.text.generator.service.BaseService;
import com.scj.text.generator.util.TextToImg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
@Service
public class BaseServiceImpl implements BaseService{
    @Autowired
    TextToImg textToImg;

    public Map<Integer,Map<String,Integer>> commit_txt_to_write_information = new HashMap<>();
    private final int[][] FONT_LIST = {{81,465},{460,458},{452},{462},{464,463},{163,446},{455,612},{612}};//预设字体列表
    private final int[] fONT_SIZE = {200,150,180};//预设字体大小
    int[] CanvasWidth={3000,2000,1936};
    int[] CanvasHeight={3600,2200,2750};
    int[] start_x={302,270,334};
    int[] start_y={875,820,380};
    int[] wide_img={3000,2000,1936};
    int[] high_img={3600,2200,2750};
    @Autowired
    commit_images_dao new_commit_images_dao;
    @Override
    public void set_process(Integer user_id)
    {
        commit_txt_to_write_information.get(user_id).put("process",0);
    }
    @Override
    public void text(Boolean start,String title, String text, String color, Integer user_id, Integer commit_id,int pageNum,int type,int background_type) throws Exception
    {

        if (start) {
            Map<String,Integer> commit_txt_to_write_information_by_commit_id = new HashMap<>();
            commit_txt_to_write_information_by_commit_id.put("process",0);//当前进度
            commit_txt_to_write_information_by_commit_id.put("length",text.length());//总工作量
            commit_txt_to_write_information.put(user_id,commit_txt_to_write_information_by_commit_id);
        }

        File file = new File("/root/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
        //File file = new File("D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + pageNum + ".png");
        if (!file.exists()) {//没有则创建
            File folder = new File("/root/images/"+user_id+"/"+commit_id+"/");
            //File folder = new File("D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\");
            if (!folder.exists())
                folder.mkdirs();//创建文件夹
            file.createNewFile();
        }
        //image_urls.put(pageNum,"101.42.173.97:8090/images/"+user_id+"/"+commit_id+"/"+"this_img"+pageNum+".png");
        //创建输出流
        //FileOutputStream out = new FileOutputStream(file);
        //创建画笔
        //创建Image
        if(background_type==-2) {
            commit_images new_commit_image = new commit_images(commit_id,"101.42.173.97:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            //commit_images new_commit_image = new commit_images(commit_id,"localhost:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            new_commit_images_dao.insert_commit_image_url(new_commit_image);
            BufferedImage image = new BufferedImage(720, 1000, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            image = g.getDeviceConfiguration().createCompatibleImage(720, 1000, Transparency.TRANSLUCENT);
            g.dispose();
            g = image.createGraphics();
            //画标题
            //算出起笔位置
            int beginX = (720 - 36 * (title.length())) / 2;
            if (beginX < 0) {
                System.out.println("标题超限,自动移到行首");
                beginX = 0;
            }
            System.out.println("起笔位置为：" + beginX);
            //创建随机数
            Random random = new Random();
            if (title == null) {
                title = "";
            }
            for (int i = 0; i < title.length(); i++) {

                char c = title.charAt(i);
                InputStream inputStream;
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                //读取图片
                try {
                    //if(check==0)
                        inputStream = textToImg.textToImg(c + "", FONT_LIST[type][randomNum], 36, 36 * 2, 36 * 2, color);
                    //else
                    //    inputStream = textToImg.textToImg(c + "", FONT_LIST[type][randomNum], 36, 36, 36*2, color);

                } catch (Exception e) {
                    //触发重试机制
                    System.out.println(e);
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                g.drawImage(fontImg, beginX, 0, 36, 36, null);
                if(type==7)
                    beginX += 18/2;
                else
                    beginX += check==1?18/2:36 / 2;
            }
            int x = 0;
            int y = "".equals(title) ? 10 : (36 / 2 + 20);
            //将正文字符串拆分
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int a = (int) commit_txt_to_write_information.get(user_id).get("process");
                commit_txt_to_write_information.get(user_id).put("process",a+1) ;
                //判断是否遇到换行符
                if (c == '\n') {
                    System.out.println("遇到换行符！");
                    x = 0;
                    y += (36 / 2 + 15);
                    //换纸
                    if (y + 36 > 1000) {
                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //关闭流
                        //out.close();
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return ;
                    }
                    continue;
                }
                String str = c + "";
                int flag = 0;//是否遇到数字
                int count = 1;//遇到的数字长度
                //判断是否遇到数字
                if (Character.isDigit(c)) {
                    System.out.println("遇到数字！");
                    flag = 1;
                    while (Character.isDigit(text.charAt(i + count))) {
                        count++;
                    }
                    System.out.println("提取长度为" + count);
                    str = text.substring(i, i + count);
                    System.out.println("提取数字为" + str);
                    i += (count - 1);
                }
                //生成随机数
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int randomX = random.nextInt(5);
                int randomY = random.nextInt(3) - 1;
                int fontSize = 36 + randomNum;
                int imgWidth = fontSize * 2;
                InputStream inputStream;
                int check = 0 ;//检查是否是字母
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                //读取图片
                try {
                    //if(check==0)
                        inputStream = textToImg.textToImg(str, flag == 1 ? 462 : FONT_LIST[type][randomNum], flag == 1 ? fontSize / 4 * 3 : fontSize, flag == 0 ? imgWidth : imgWidth * count / 2, imgWidth, color);
                    //else
                    //    inputStream = textToImg.textToImg(str, FONT_LIST[type][randomNum], fontSize, imgWidth  / 2, imgWidth, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                //if(check==0)
                    g.drawImage(fontImg, count > 4 ? x - 30 : x + randomX, y + randomY, flag == 0 ? 36 : 36 * count / 2, 36, null);
                //else
                  //  g.drawImage(fontImg, x , y , 36/ 2, 36, null);
                if (count > 2) {
                    x += (imgWidth - 36 * 3 / 2) * count / 2 - 10;
                } else {
                    if(type==7)
                        x += (imgWidth - 36 * 3 / 2)/2;
                    else if(check==0)
                        x += (imgWidth - 36 * 3 / 2);
                    else
                        x += (imgWidth - 36 * 3 / 2)/2;
                }
                if (x + 36 > 720) {
                    x = 0;
                    y += (imgWidth - 36 * 3 / 2 + 15);
                    if (y + 36 > 1000) {
                        //换纸
                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //关闭流
                        //out.close();
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return;
                    }
                }
            }
            //释放资源
            g.dispose();
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(image);
            ImageIO.write(image, "png", file);
            //关闭流
            //out.close();
            System.out.println("完成!");
        }
        else if(background_type==-1)
        {
            commit_images new_commit_image = new commit_images(commit_id,"101.42.173.97:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            //commit_images new_commit_image = new commit_images(commit_id,"localhost:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            new_commit_images_dao.insert_commit_image_url(new_commit_image);
            BufferedImage image = new BufferedImage(720, 1000, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            image = g.getDeviceConfiguration().createCompatibleImage(720, 1000, Transparency.TRANSLUCENT);
            g.dispose();
            g.setColor(Color.WHITE);
            g.fillRect(0,0,720,1000);
            g = image.createGraphics();
            //画标题
            //算出起笔位置
            int beginX = (720 - 36 * (title.length())) / 2;
            if (beginX < 0) {
                System.out.println("标题超限,自动移到行首");
                beginX = 0;
            }
            System.out.println("起笔位置为：" + beginX);
            //创建随机数
            Random random = new Random();
            if (title == null) {
                title = "";
            }
            for (int i = 0; i < title.length(); i++) {

                char c = title.charAt(i);
                InputStream inputStream;
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                //读取图片
                try {
                    inputStream = textToImg.textToImg(c + "", FONT_LIST[type][randomNum], 36, 36 * 2, 36 * 2, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println(e);
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                g.drawImage(fontImg, beginX, 0,36, 36, null);
                if(type==7)
                    beginX +=18/2;
                else
                    beginX += check==1?18/2:36 / 2;
            }
            int x = 0;
            int y = "".equals(title) ? 10 : (36 / 2 + 20);
            //将正文字符串拆分
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int a = (int) commit_txt_to_write_information.get(user_id).get("process");
                commit_txt_to_write_information.get(user_id).put("process",a+1) ;
                //判断是否遇到换行符
                if (c == '\n') {
                    System.out.println("遇到换行符！");
                    x = 0;
                    y += (36 / 2 + 15);
                    //换纸
                    if (y + 36 > 1000) {
                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //关闭流
                        //out.close();
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return ;
                    }
                    continue;
                }
                String str = c + "";
                int flag = 0;//是否遇到数字
                int count = 1;//遇到的数字长度
                //判断是否遇到数字
                if (Character.isDigit(c)) {
                    System.out.println("遇到数字！");
                    flag = 1;
                    while (Character.isDigit(text.charAt(i + count))) {
                        count++;
                    }
                    System.out.println("提取长度为" + count);
                    str = text.substring(i, i + count);
                    System.out.println("提取数字为" + str);
                    i += (count - 1);
                }
                //生成随机数
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int randomX = random.nextInt(5);
                int randomY = random.nextInt(3) - 1;
                int fontSize = 36 + randomNum;
                int imgWidth = fontSize * 2;
                InputStream inputStream;
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                //读取图片
                try {
                   // if(check==0)
                        inputStream = textToImg.textToImg(str, flag == 1 ? 462 : FONT_LIST[type][randomNum], flag == 1 ? fontSize / 4 * 3 : fontSize, flag == 0 ? imgWidth : imgWidth * count / 2, imgWidth, color);
                    //else
                      //  inputStream = textToImg.textToImg(str, FONT_LIST[type][randomNum], fontSize, imgWidth / 2, imgWidth, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                //if(check==0)
                    g.drawImage(fontImg, count > 4 ? x - 30 : x + randomX, y + randomY, flag == 0 ? 36 : 36 * count / 2, 36, null);
                //else
                //    g.drawImage(fontImg, x + randomX, y , 36/ 2, 36, null);
                if (count > 2) {
                    x += (imgWidth - 36 * 3 / 2) * count / 2 - 10;
                } else {
                    if(type==7)
                        x += (imgWidth - 36 * 3 / 2)/2;
                    else if(check==0)
                        x += (imgWidth - 36 * 3 / 2);
                    else
                        x += (imgWidth - 36 * 3 / 2)/2;
                }

                if (x + 36 > 720) {
                    x = 0;
                    y += (imgWidth - 36 * 3 / 2 + 15);
                    if (y + 36 > 1000) {
                        //换纸
                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //关闭流
                        //out.close();
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return;
                    }
                }
            }
            //释放资源
            g.dispose();
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(image);
            ImageIO.write(image, "png", file);
            //关闭流
            //out.close();
            System.out.println("完成!");
        }
        else if(background_type==1) {
            commit_images new_commit_image = new commit_images(commit_id,"101.42.173.97:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            //commit_images new_commit_image = new commit_images(commit_id,"localhost:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            new_commit_images_dao.insert_commit_image_url(new_commit_image);
            int canvasWidth = CanvasWidth[background_type];
            int canvasHeight = CanvasHeight[background_type];
            int FONT_SIZE = fONT_SIZE[background_type];
            BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            image = g.getDeviceConfiguration().createCompatibleImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);
            g.dispose();
            g = image.createGraphics();
            //透明背景
            //画标题
            //算出起笔位置

            int beginX = (canvasWidth - FONT_SIZE * (title.length())) / 2;
            if (beginX < 0) {
                System.out.println("标题超限,自动移到行首");
                beginX = 0;
            }
            System.out.println("起笔位置为：" + beginX);
            //创建随机数
            Random random = new Random();
            if (title == null) {
                title = "";
            }
            for (int i = 0; i < title.length(); i++) {

                char c = title.charAt(i);
                InputStream inputStream;
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                //读取图片
                try {
                    inputStream = textToImg.textToImg(c + "", FONT_LIST[type][randomNum], FONT_SIZE, FONT_SIZE * 2, FONT_SIZE * 2, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println(e);
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                g.drawImage(fontImg, beginX, 0,FONT_SIZE, FONT_SIZE, null);
                if(type==7)
                    beginX += FONT_SIZE/4;
                else
                    beginX += check==1?FONT_SIZE/4:FONT_SIZE / 2;
            }
            int x = 0;
            int y = "".equals(title) ? 10 : (FONT_SIZE *3/ 5 + 20);
            //将正文字符串拆分
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int a = (int) commit_txt_to_write_information.get(user_id).get("process");
                commit_txt_to_write_information.get(user_id).put("process",a+1) ;
                //判断是否遇到换行符
                if (c == '\n') {
                    System.out.println("遇到换行符！");
                    x = 0;
                    y += (3*FONT_SIZE / 5);
                    //换纸
                    if (y + FONT_SIZE > canvasHeight) {

                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //generate("C:\\Users\\MSI-NB\\Desktop\\东秦信纸.jpg",
                          //      "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + String.valueOf(pageNum-1) + ".png"
                        //,background_type);
                        generate("/root/background/东秦信纸.jpg",
                                "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum-1)+".png",background_type);
                        //关闭流
                        //out.close();
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return ;
                    }
                    continue;
                }
                String str = c + "";
                int flag = 0;//是否遇到数字
                int count = 1;//遇到的数字长度
                //判断是否遇到数字
                if (Character.isDigit(c)) {
                    System.out.println("遇到数字！");
                    flag = 1;
                    while (Character.isDigit(text.charAt(i + count))) {
                        count++;
                    }
                    System.out.println("提取长度为" + count);
                    str = text.substring(i, i + count);
                    System.out.println("提取数字为" + str);
                    i += (count - 1);
                }
                //生成随机数
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int randomX = random.nextInt(10);
                int randomY = random.nextInt(10) - 5;
                int fontSize = FONT_SIZE + randomNum;
                int imgWidth = fontSize * 2;
                InputStream inputStream;
                //读取图片
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                try {
                    //if(check==0)
                        inputStream = textToImg.textToImg(str, flag == 1 ? 462 : FONT_LIST[type][randomNum], flag == 1 ? fontSize / 4 * 3 : fontSize, flag == 0 ? imgWidth : imgWidth * count / 2, imgWidth, color);
                    //else
                    //    inputStream = textToImg.textToImg(str, FONT_LIST[type][randomNum],fontSize,  imgWidth  / 2+10, imgWidth, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                //if(check==0)
                    g.drawImage(fontImg, count > 4 ? x - 30 : x + randomX, y + randomY, flag == 0 ? FONT_SIZE : FONT_SIZE * count / 2, FONT_SIZE, null);
                //else
                //    g.drawImage(fontImg, x + randomX, y , FONT_SIZE  / 2, FONT_SIZE, null);
                if (count > 2) {
                    x += (imgWidth - FONT_SIZE * 3 / 2) * count / 2 - 10;
                } else {
                    if(type==7)
                        x += (imgWidth - FONT_SIZE * 3 / 2)/2;
                    else if(check==0)
                        x += (imgWidth - FONT_SIZE * 3 / 2);
                    else
                        x += (imgWidth - FONT_SIZE * 3 / 2)/2;
                }

                if (x + FONT_SIZE > canvasWidth) {
                    x = 0;
                    y += (imgWidth - FONT_SIZE * 7 / 5);
                    if (y + FONT_SIZE > canvasHeight) {
                        //换纸
                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //generate("C:\\Users\\MSI-NB\\Desktop\\东秦信纸.jpg",
                          //      "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + String.valueOf(pageNum-1) + ".png"
                            //    ,background_type);
                        generate("/root/background/东秦信纸.jpg",
                                "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum-1)+".png",background_type);
                        //关闭流
                        //out.close();
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return;
                    }
                }
            }
            //释放资源
            g.dispose();
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(image);
            ImageIO.write(image, "png", file);
            //关闭流
            //out.close();
            System.out.println("完成!");
            //generate("C:\\Users\\MSI-NB\\Desktop\\东秦信纸.jpg",
              //      "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + String.valueOf(pageNum) + ".png"
                //    ,background_type);
            generate("/root/background/东秦信纸.jpg",
                    "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum)+".png",background_type);
            return;
        }
        else if(background_type==0) {
            commit_images new_commit_image = new commit_images(commit_id,"101.42.173.97:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            //commit_images new_commit_image = new commit_images(commit_id,"localhost:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            new_commit_images_dao.insert_commit_image_url(new_commit_image);
            int canvasWidth = CanvasWidth[background_type];
            int canvasHeight = CanvasHeight[background_type];
            int FONT_SIZE = fONT_SIZE[background_type];
            BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            image = g.getDeviceConfiguration().createCompatibleImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);
            g.dispose();
            g = image.createGraphics();
            //透明背景
            //画标题
            //算出起笔位置

            int beginX = (canvasWidth - FONT_SIZE * (title.length())) / 2;
            if (beginX < 0) {
                System.out.println("标题超限,自动移到行首");
                beginX = 0;
            }
            System.out.println("起笔位置为：" + beginX);
            //创建随机数
            Random random = new Random();
            if (title == null) {
                title = "";
            }
            for (int i = 0; i < title.length(); i++) {

                char c = title.charAt(i);
                InputStream inputStream;
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                //读取图片
                try {
                    //if(check==0)
                        inputStream = textToImg.textToImg(c + "", FONT_LIST[type][randomNum], FONT_SIZE, FONT_SIZE * 2, FONT_SIZE * 2, color);
                    //else
                    //    inputStream = textToImg.textToImg(c + "", FONT_LIST[type][randomNum], FONT_SIZE, FONT_SIZE, FONT_SIZE * 2, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println(e);
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                g.drawImage(fontImg, beginX, 0, FONT_SIZE, FONT_SIZE, null);
                if(type==7)
                    beginX += FONT_SIZE / 2;
                else
                    beginX += check==1?FONT_SIZE/4:FONT_SIZE / 2;
            }
            int x = 0;
            int y = "".equals(title) ? 10 : (FONT_SIZE *3/ 5 + 20);
            //将正文字符串拆分
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int a = (int) commit_txt_to_write_information.get(user_id).get("process");
                commit_txt_to_write_information.get(user_id).put("process",a+1) ;
                //判断是否遇到换行符
                if (c == '\n') {
                    System.out.println("遇到换行符！");
                    x = 0;
                    y += (3*FONT_SIZE / 5 );
                    //换纸
                    if (y + FONT_SIZE > canvasHeight) {

                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //generate("C:\\Users\\MSI-NB\\Desktop\\1.png",
                        //        "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + String.valueOf(pageNum-1) + ".png"
                        //        ,background_type);
                        //关闭流
                        //out.close();
                        generate("/root/background/1.png",
                                "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum-1)+".png",background_type);
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return ;
                    }
                    continue;
                }
                String str = c + "";
                int flag = 0;//是否遇到数字
                int count = 1;//遇到的数字长度
                //判断是否遇到数字
                if (Character.isDigit(c)) {
                    System.out.println("遇到数字！");
                    flag = 1;
                    while (Character.isDigit(text.charAt(i + count))) {
                        count++;
                    }
                    System.out.println("提取长度为" + count);
                    str = text.substring(i, i + count);
                    System.out.println("提取数字为" + str);
                    i += (count - 1);
                }
                //生成随机数
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int randomX = random.nextInt(10);
                int randomY = random.nextInt(10) - 5;
                int fontSize = FONT_SIZE + randomNum;
                int imgWidth = fontSize * 2;
                InputStream inputStream;
                //读取图片
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                try {
                    //if(check==0)
                        inputStream = textToImg.textToImg(str, flag == 1 ? 462 : FONT_LIST[type][randomNum], flag == 1 ? fontSize / 4 * 3 : fontSize, flag == 0 ? imgWidth : imgWidth * count / 2, imgWidth, color);
                    //else
                      //  inputStream = textToImg.textToImg(str, flag == 1 ? 462 : FONT_LIST[type][randomNum],fontSize, imgWidth / 2, imgWidth, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                //if(check==0)
                    g.drawImage(fontImg, count > 4 ? x - 30 : x + randomX, y + randomY, flag == 0 ? FONT_SIZE : FONT_SIZE * count / 2, FONT_SIZE, null);
                //else
                //    g.drawImage(fontImg, x, y , FONT_SIZE  / 2, FONT_SIZE, null);
                if (count > 2) {
                    x += (imgWidth - FONT_SIZE * 3 / 2) * count / 2 - 10;
                } else {
                    if(type==7)
                        x += (imgWidth - FONT_SIZE * 3 / 2)/2;
                    else if(check==0)
                        x += (imgWidth - FONT_SIZE * 3 / 2);
                    else
                        x += (imgWidth - FONT_SIZE * 3 / 2)/2;
                }

                if (x + FONT_SIZE > canvasWidth) {
                    x = 0;
                    y += (imgWidth - FONT_SIZE * 7 / 5 );
                    if (y + FONT_SIZE > canvasHeight) {
                        //换纸
                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //generate("C:\\Users\\MSI-NB\\Desktop\\1.png",
                        //        "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + String.valueOf(pageNum-1) + ".png"
                        //        ,background_type);
                        generate("/root/background/1.png",
                                "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum-1)+".png",background_type);
                        //关闭流
                        //out.close();
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return;
                    }
                }
            }
            //释放资源
            g.dispose();
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(image);
            ImageIO.write(image, "png", file);
            //关闭流
            //out.close();
            System.out.println("完成!");
            //generate("C:\\Users\\MSI-NB\\Desktop\\1.png",
            //        "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + pageNum + ".png"
            //        ,background_type);
            generate("/root/background/1.png",
                    "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum)+".png",background_type);
            return;
        }
        else if(background_type==2) {
            commit_images new_commit_image = new commit_images(commit_id,"101.42.173.97:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            //commit_images new_commit_image = new commit_images(commit_id,"localhost:8090/images/"+user_id+"/"+commit_id+"/"+"img"+pageNum+".png");
            new_commit_images_dao.insert_commit_image_url(new_commit_image);
            int canvasWidth = CanvasWidth[background_type];
            int canvasHeight = CanvasHeight[background_type];
            int FONT_SIZE = fONT_SIZE[background_type];
            BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            image = g.getDeviceConfiguration().createCompatibleImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);
            g.dispose();
            g = image.createGraphics();
            //透明背景
            //画标题
            //算出起笔位置

            int beginX = (canvasWidth - FONT_SIZE * (title.length())) / 2;
            if (beginX < 0) {
                System.out.println("标题超限,自动移到行首");
                beginX = 0;
            }
            System.out.println("起笔位置为：" + beginX);
            //创建随机数
            Random random = new Random();
            if (title == null) {
                title = "";
            }
            for (int i = 0; i < title.length(); i++) {

                char c = title.charAt(i);
                InputStream inputStream;
                int randomNum = random.nextInt(FONT_LIST[type].length);
                //读取图片
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                try {
                    inputStream = textToImg.textToImg(c + "", FONT_LIST[type][randomNum], FONT_SIZE, FONT_SIZE * 2, FONT_SIZE * 2, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println(e);
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                g.drawImage(fontImg, beginX, 0, FONT_SIZE, FONT_SIZE, null);
                if(type==7)
                    beginX += FONT_SIZE/4;
                else
                    beginX += check==1?FONT_SIZE/4:FONT_SIZE / 2;
            }
            int x = 0;
            int y = "".equals(title) ? 10 : (FONT_SIZE *3/ 5 )+7;
            //将正文字符串拆分
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                int a = (int) commit_txt_to_write_information.get(user_id).get("process");
                commit_txt_to_write_information.get(user_id).put("process",a+1) ;
                //判断是否遇到换行符
                if (c == '\n') {
                    System.out.println("遇到换行符！");
                    x = 0;
                    y += (FONT_SIZE*4/ 7+29);
                    //换纸
                    if (y + FONT_SIZE > canvasHeight) {

                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //generate("C:\\Users\\MSI-NB\\Desktop\\2.png",
                          //      "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + String.valueOf(pageNum-1) + ".png"
                            //    ,background_type);
                        //关闭流
                        //out.close();
                        generate("/root/background/2.png",
                                "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum-1)+".png",background_type);
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return ;
                    }
                    continue;
                }
                String str = c + "";
                int flag = 0;//是否遇到数字
                int count = 1;//遇到的数字长度
                //判断是否遇到数字
                if (Character.isDigit(c)) {
                    System.out.println("遇到数字！");
                    flag = 1;
                    while (Character.isDigit(text.charAt(i + count))) {
                        count++;
                    }
                    System.out.println("提取长度为" + count);
                    str = text.substring(i, i + count);
                    System.out.println("提取数字为" + str);
                    i += (count - 1);
                }
                //生成随机数
                int randomNum = random.nextInt(FONT_LIST[type].length);
                int randomX = random.nextInt(10);
                int randomY = random.nextInt(10) - 5;
                int fontSize = FONT_SIZE + randomNum;
                int imgWidth = fontSize * 2+29;
                InputStream inputStream;
                int check=0;
                if(('A'<=c&&'Z'>=c)||('a'<=c&&'z'>=c))
                {
                    check=1;
                }
                //读取图片
                try {
                    //if(check==0)
                        inputStream = textToImg.textToImg(str, flag == 1 ? 462 : FONT_LIST[type][randomNum], flag == 1 ? fontSize / 4 * 3 : fontSize, flag == 0 ? imgWidth : imgWidth * count / 2, imgWidth, color);
                   // else
                     //   inputStream = textToImg.textToImg(str, flag == 1 ? 462 : FONT_LIST[type][randomNum], fontSize, imgWidth / 2, imgWidth, color);
                } catch (Exception e) {
                    //触发重试机制
                    System.out.println("3秒后重试。。。");
                    Thread.sleep(3000);
                    i--;
                    continue;
                }

                BufferedImage fontImg = ImageIO.read(inputStream);

                //绘制合成图像
                //if(check==0)
                    g.drawImage(fontImg, count > 4 ? x - 30 : x + randomX, y + randomY, flag == 0 ? FONT_SIZE : FONT_SIZE * count / 2, FONT_SIZE, null);
                //else
                //    g.drawImage(fontImg,  x + randomX, y , FONT_SIZE  / 2, FONT_SIZE+15, null);
                if (count > 2) {
                    x += (imgWidth - FONT_SIZE * 3 / 2) * count / 2 - 10;
                } else {
                    if(type==7)
                        x += (imgWidth - FONT_SIZE * 3 / 2)/2;
                    else if(check==0)
                        x += (imgWidth - FONT_SIZE * 3 / 2);
                    else
                        x += (imgWidth - FONT_SIZE * 3 / 2)/2;
                }

                if (x + FONT_SIZE > canvasWidth) {
                    x = 0;
                    y += (imgWidth - FONT_SIZE  *10/7 );
                    if (y + FONT_SIZE > canvasHeight) {
                        //换纸
                        pageNum++;
                        //释放资源
                        g.dispose();
                        //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        //encoder.encode(image);
                        ImageIO.write(image, "png", file);
                        //generate("C:\\Users\\MSI-NB\\Desktop\\2.png",
                          //      "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + String.valueOf(pageNum-1) + ".png"
                            //    ,background_type);
                        //关闭流
                        //out.close();
                        generate("/root/background/2.png",
                                "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum-1)+".png",background_type);
                        System.out.println("完成!");
                        text(false, "", text.substring(i + 1), color, user_id, commit_id,pageNum,type,background_type);
                        return;
                    }
                }
            }
            //释放资源
            g.dispose();
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(image);
            ImageIO.write(image, "png", file);
            //关闭流
            //out.close();
            System.out.println("完成!");
            //generate("C:\\Users\\MSI-NB\\Desktop\\2.png",
              //      "D:\\学习资料\\安璟坤的项目\\手写大师\\penalty-writing-terminator\\text-generator\\src\\main\\resources\\images\\" + user_id + "\\" + commit_id + "\\" + "img" + pageNum + ".png"
               //     ,background_type);
            generate("/root/background/2.png",
                    "/root/images/"+user_id+"/"+commit_id+"/"+"img"+String.valueOf(pageNum)+".png",background_type);
            return;
        }
    }
    @Autowired
    user_dao this_user_dao;
    @Override
    public int user_is_exist(Integer user_id)
    {
        if(this_user_dao.select_user_by_user_id(user_id)!=null)
            return 1;
        else
            return 0;
    }
    public static Map<Integer, Object> getObjectToMap(Object obj) throws IllegalAccessException {
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            int keyName = Integer.parseInt(field.getName());
            Object value = field.get(obj);
            if (value == null)
                value = "";
            map.put(keyName, value);
        }
        return map;
    }
    @Override
    public double get_process(Integer user_id)
    {
        Double process = commit_txt_to_write_information.get(user_id).get("process").doubleValue();
        Double all = commit_txt_to_write_information.get(user_id).get("length").doubleValue();
        return process>=all?100:(process/all)*100;
    }
    public File generate(String background,String water,int background_type) throws IOException{
        // 底图
        File sourceFile = new File(background);
        // 水印图片
        File waterFile = new File(water);
        String FIN = water;
        // 最终合成


        BufferedImage buffImg = watermark(sourceFile, waterFile, 1.0f,background_type);
        generateWaterFile(buffImg, FIN);

        return new File(water);
    }
    private void generateWaterFile(BufferedImage buffImg, String savePath) {
        int temp = savePath.lastIndexOf(".") + 1;
        try {
            ImageIO.write(buffImg, savePath.substring(temp), new File(savePath));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    private BufferedImage watermark(File file, File waterFile, float alpha,int background_type) throws IOException {
        // 获取底图
        BufferedImage buffImg = ImageIO.read(file);
        int buffImgWidth = buffImg.getWidth(); //获取底图的宽度
        int buffImgHight = buffImg.getHeight();//获取底图的高度

        // 获取层图1
        BufferedImage waterImg1 = ImageIO.read(waterFile);
        // 创建Graphics2D对象，用在底图对象上绘图
        Graphics2D g2d = buffImg.createGraphics();
        int waterImgWidth1 = waterImg1.getWidth();// 获取层图的宽度
        int waterImgHeight1 = waterImg1.getHeight();// 获取层图的高度
        // 在图形和图像中实现混合和透明效果
        //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
        // 绘制
        //g2d.drawImage(waterImg1, 775,  450, waterImgWidth1, waterImgHeight1, null);
        g2d.drawImage(waterImg1, start_x[background_type],  start_y[background_type], wide_img[background_type], high_img[background_type], null);


        g2d.dispose();// 释放图形上下文使用的系统资源
        return buffImg;
    }
}
