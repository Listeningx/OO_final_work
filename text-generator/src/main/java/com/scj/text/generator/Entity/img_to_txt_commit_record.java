package com.scj.text.generator.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class img_to_txt_commit_record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer img_to_txt_commit_record_id;
    Integer user_id;
    String commit_image_url;
    String txt;
    Date commit_time;

    public img_to_txt_commit_record(Integer user_id, String commit_image_url, String txt,Date commit_time) {
        this.user_id = user_id;
        this.commit_image_url = commit_image_url;
        this.txt = txt;
        this.commit_time=commit_time;
    }

    public Date getCommit_time() {
        return commit_time;
    }

    public void setCommit_time(Date commit_time) {
        this.commit_time = commit_time;
    }

    public img_to_txt_commit_record() {
    }

    public Integer getImg_to_txt_commit_record_id() {
        return img_to_txt_commit_record_id;
    }

    public void setImg_to_txt_commit_record_id(Integer img_to_txt_commit_record_id) {
        this.img_to_txt_commit_record_id = img_to_txt_commit_record_id;
    }


    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getCommit_image_url() {
        return commit_image_url;
    }

    public void setCommit_image_url(String commit_image_url) {
        this.commit_image_url = commit_image_url;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
