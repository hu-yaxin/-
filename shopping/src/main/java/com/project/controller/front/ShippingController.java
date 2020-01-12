package com.project.controller.front;


import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.pojo.Shipping;
import com.project.pojo.User;
import com.project.service.IShippingService;
import com.project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    IShippingService shippingService;

    @RequestMapping("add.do")
    public ServerResponse add(Shipping shipping, HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        shipping.setUserId(user.getId());
        return shippingService.add(shipping);
    }
    @RequestMapping("del.do")
    public ServerResponse del(Integer shippingId, HttpSession session){

        return shippingService.del(shippingId);
    }
    @RequestMapping("update.do")
    public ServerResponse update(Shipping shipping, HttpSession session){

        return shippingService.update(shipping);
    }
    @RequestMapping("select.do")
    public ServerResponse select(Integer shippingId, HttpSession session){

        return shippingService.select(shippingId);
    }
    @RequestMapping("list.do")
    public ServerResponse list(@RequestParam(name ="pageNum",required = false, defaultValue = "1")Integer pageNum,
                               @RequestParam(name ="pageSize",required = false,defaultValue="10")Integer pageSize, HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);

        return shippingService.list(user.getId(),pageNum,pageSize);
    }
}
