package com.project.controller.backend;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.project.common.ServerResponse;
import com.project.pojo.User;
import com.project.service.IOrderService;
import com.project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/manager/")
public class ManagerOrderController {
    @Autowired
    IOrderService orderService;

    @RequestMapping("list.do")
    public ServerResponse list(@RequestParam(name ="pageNum",required = false, defaultValue = "1")Integer pageNum,
                               @RequestParam(name ="pageSize",required = false,defaultValue="10")Integer pageSize, HttpSession session){

        return orderService.AllList(pageNum,pageSize);
    }
    @RequestMapping("detail.do/{orderNo}")
    public ServerResponse detail(@PathVariable("orderNo")Long orderNo, HttpSession session){

        return orderService.detail(orderNo);
    }
    @RequestMapping("set_status")
    public ServerResponse set_status(Long orderNo,Integer status ){

        return orderService.setStatus(orderNo,status);
    }
    @RequestMapping("getOrder")
    public ServerResponse getOrder(Long orderNo ){

        return orderService.getOrder(orderNo);
    }

}
