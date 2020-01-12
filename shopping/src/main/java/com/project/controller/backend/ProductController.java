package com.project.controller.backend;


import com.project.common.ResponseCode;
import com.project.common.RoleEnum;
import com.project.common.ServerResponse;
import com.project.pojo.Product;
import com.project.pojo.User;
import com.project.service.impl.ProductServiceImpl;
import com.project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manager/product/")
public class ProductController {
    @Autowired
    ProductServiceImpl productService;
    /**
     * 商品的添加或更新
     */
    @RequestMapping(value ="save.do")
    public ServerResponse addOrUpdate(Product product){

        return productService.addOrUpdate(product);
    }

    /**
     * 产品上下架
     * @return
     */
    @RequestMapping(value ="set_sale_status.do")
    public ServerResponse set_sale_status(int id,int status){


        return productService.setSaleStatus(id,status);
    }

    /**
     * 产品搜索
     * productName
     * productId
     * pageNum(default=1)
     * pageSize(default=10)
     */
    @RequestMapping(value ="search.do")
    public ServerResponse search(@RequestParam(name ="productName",required=false)String productName,
                                 @RequestParam(name ="productId",required = false)Integer productId,
                                 @RequestParam(name ="pageNum",required = false, defaultValue = "1")Integer pageNum,
                                 @RequestParam(name ="pageSize",required = false,defaultValue="10")Integer pageSize){
        System.out.println(pageNum);
        return productService.search(productName, productId, pageNum, pageSize);
    }

    @RequestMapping(value = "/{productId}")
    public ServerResponse detail(@PathVariable("productId") Integer productId){

        return productService.detail(productId);
    }
}
