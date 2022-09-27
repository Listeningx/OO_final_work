package com.scj.text.generator.service;

import java.util.List;
import java.util.Map;

public interface get_commit_list {
    public List<Map<String,Object>> get_commit_list_by_user_id(Integer user_id);
    public List<Map<String,Object>> get_image_urls_list(Integer commit_id);
    public void delete_commit(Integer commit_id,Integer user_id);
}
