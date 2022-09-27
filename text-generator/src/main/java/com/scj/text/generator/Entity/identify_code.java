package com.scj.text.generator.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class identify_code {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identify_code_id;

    private String identify_code;
    private String email;
    private int email_type;

    public identify_code() {
    }

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    public identify_code(String identify_code, String email, int email_type, Date date) {
        this.identify_code = identify_code;
        this.email = email;
        this.email_type = email_type;
        this.date = date;
    }

    public Integer getIdentify_code_id() {
        return identify_code_id;
    }

    public void setIdentify_code_id(Integer identify_code_id) {
        this.identify_code_id = identify_code_id;
    }

    public String getIdentify_code() {
        return identify_code;
    }

    public void setIdentify_code(String identify_code) {
        this.identify_code = identify_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmail_type() {
        return email_type;
    }

    public void setEmail_type(int email_type) {
        this.email_type = email_type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
