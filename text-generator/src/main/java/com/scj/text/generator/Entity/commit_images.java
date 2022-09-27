package com.scj.text.generator.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class commit_images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commit_image_id;

    private Integer commit_id;

    private String image_url;

    public commit_images(Integer commit_id, String image_url) {
        this.commit_id = commit_id;
        this.image_url = image_url;
    }

    public commit_images() {
    }

    public Integer getCommit_image_id() {
        return commit_image_id;
    }

    public void setCommit_image_id(Integer commit_image_id) {
        this.commit_image_id = commit_image_id;
    }

    public Integer getCommit_id() {
        return commit_id;
    }

    public void setCommit_id(Integer commit_id) {
        this.commit_id = commit_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
