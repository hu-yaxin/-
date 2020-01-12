package com.project.controller.front;


import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.pojo.Cart;
import com.project.pojo.Product;
import com.project.pojo.User;
import com.project.service.ICartService;
import com.project.service.IProductService;
import com.project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    ICartService cartService;


    /**
     * 添加商品到购物车
     */
    @RequestMapping("add/{productId}/{count}")
    public ServerResponse addCart(@PathVariable("productId") Integer productId,
                                  @PathVariable("count") Integer count,
                                  HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        return cartService.addProductToCart(user.getId(),productId,count);
    }
    /**
     * 购物车列表
     */
    @RequestMapping("list.do")
    public ServerResponse list(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        return cartService.findCartByUserid(user.getId());
    }
    /**
     * 更新购物车某个商品数量
     */
    @RequestMapping("update.do")
    public ServerResponse update(Integer productId,
                                 @RequestParam(name ="count",required = false)Integer count,
                                 @RequestParam(name ="checked",required = false,defaultValue = "1")Integer checked,
                                 HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        System.out.println("productId:"+productId+"count:"+count+"checked:"+checked);
        return cartService.updateCartByUseridAndProductid(user.getId(),productId,count,checked);
    }
    /**
     * 全选
     */
    @RequestMapping("select_all.do")
    public ServerResponse select_all(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);


        return cartService.select_all(user.getId());
    }
    /**
     * 全选
     */
    @RequestMapping("un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);


        return cartService.un_select_all(user.getId());
    }
    /**
     * 删除购物车已选中的商品
     */
    @RequestMapping("deleteBath.do")
    public ServerResponse deleteBath(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);

        List<Cart> carts=cartService.findCartsByUserIdAndChecked(user.getId()).getData();

        return cartService.deleteBath(carts);
    }

}
