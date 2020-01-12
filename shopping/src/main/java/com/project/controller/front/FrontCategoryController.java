package com.project.controller.front;


import com.project.common.ServerResponse;
import com.project.service.impl.CategoryServiceImpl;
import com.project.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category/")
public class FrontCategoryController {
    @Autowired
    CategoryServiceImpl categoryService;
    /**
     * 查询商品类别
     */
    @RequestMapping(value ="showCategory")
    public ServerResponse showCategory(){
        return categoryService.showCategory();
    }


}
