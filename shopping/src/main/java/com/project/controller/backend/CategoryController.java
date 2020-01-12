package com.project.controller.backend;


import com.project.common.ResponseCode;
import com.project.common.RoleEnum;
import com.project.common.ServerResponse;
import com.project.pojo.Category;
import com.project.pojo.User;
import com.project.service.impl.CategoryServiceImpl;
import com.project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manager/category/")
public class CategoryController {

    @Autowired
    CategoryServiceImpl categoryService;
    /**
     * 添加类别
     */
    @RequestMapping(value="add_category.do")
    public ServerResponse addCategory(Category category){

        return categoryService.addCategory(category);
    }
    /**
     * 修改类别
     */
    @RequestMapping(value="set_category_name.do")
    public ServerResponse updateCategory(Category category){

        return categoryService.updateCategory(category);
    }
    /**
     * 查看平级类别
     */
    @RequestMapping("{categoryId}")
    public ServerResponse getCategory(@PathVariable("categoryId") Integer categoryId){

        return categoryService.getCategory(categoryId);
    }
    /**
     * 递归查询
     */
    @RequestMapping("deep/{categoryId}")
    public ServerResponse deepCategory(@PathVariable("categoryId") Integer categoryId){

        return categoryService.deepCategory(categoryId);
    }
}
