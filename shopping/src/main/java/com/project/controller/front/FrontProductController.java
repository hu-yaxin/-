package com.project.controller.front;


import com.project.common.ServerResponse;
import com.project.pojo.Product;
import com.project.service.impl.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class FrontProductController {
    @Autowired
    ProductServiceImpl productService;
    /**
     * 查询热销商品
     */
    @RequestMapping(value ="/showHot")
    public ServerResponse showHot(){
        System.out.println("=========================");
        return productService.showHot();
    }

    @RequestMapping(value = "/{productId}")
    public ServerResponse detail(@PathVariable("productId") Integer productId){

        return productService.detail(productId);
    }


}
