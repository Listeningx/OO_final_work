package com.scj.text.generator.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class commit_record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commit_id;

    private Integer user_id;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commit_time;

    private String title;

    public commit_record(Integer user_id, Date commit_time, String title) {
        this.user_id = user_id;
        this.commit_time = commit_time;
        this.title = title;
    }

    public commit_record() {
    }

    public Integer getCommit_id() {
        return commit_id;
    }

    public void setCommit_id(Integer commit_id) {
        this.commit_id = commit_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Date getCommit_time() {
        return commit_time;
    }

    public void setCommit_time(Date commit_time) {
        this.commit_time = commit_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

