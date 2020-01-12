package com.project.service;

import com.project.common.ServerResponse;
import com.project.pojo.Category;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public interface ICategoryService {
    /**
     * 添加类别
     */
    public ServerResponse addCategory(Category category);
    /**
     * 修改类别
     */
    public ServerResponse updateCategory(Category category);
    /**
     * 查看平级类别
     */
    public ServerResponse getCategory(@PathVariable("categoryId") Integer categoryId);
    /**
     * 递归查询
     */
    public ServerResponse deepCategory(@PathVariable("categoryId") Integer categoryId);

    public ServerResponse<Category> selectCategory(Integer categoryId);

    public ServerResponse<Category> showCategory();

}
