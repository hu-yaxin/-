package com.project.controller.front;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.project.common.ResponseCode;
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
import java.util.Set;

@RestController
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    IOrderService orderService;
    /**
     * 创建订单
     */
    @RequestMapping("{shippingId}")
    public ServerResponse createOrder(@PathVariable("shippingId")Integer shippingId, HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        return orderService.createOrder(user.getId(),shippingId);
    }
    @RequestMapping("get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        return orderService.get_order_cart_product(user.getId());
    }

    @RequestMapping("list.do")
    public ServerResponse list(@RequestParam(name ="pageNum",required = false, defaultValue = "1")Integer pageNum,
                               @RequestParam(name ="pageSize",required = false,defaultValue="10")Integer pageSize, HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);

        return orderService.list(user.getId(),pageNum,pageSize);
    }
    @RequestMapping("detail.do/{orderNo}")
    public ServerResponse detail(@PathVariable("orderNo")Long orderNo, HttpSession session){

        return orderService.detail(orderNo);
    }
    @RequestMapping("cancel.do/{orderNo}")
    public ServerResponse cancel(@PathVariable("orderNo")Long orderNo, HttpSession session){

        return orderService.cancel(orderNo);
    }
    @RequestMapping("set_status")
    public ServerResponse set_status(Long orderNo,Integer status ){

        return orderService.setStatus(orderNo,status);
    }

    /**
     * 支付接口
     */
    @RequestMapping("pay.do/{orderNo}")
    public ServerResponse pay(@PathVariable("orderNo") Long orderNo,HttpSession session){
        return orderService.pay(orderNo);
    }
    /**
     * 支付宝服务器回调商家服务器接口
     */
    @RequestMapping("callback.do")
    public String alipay_callback(HttpServletRequest request){
        Map<String,String[]> callbackParam=request.getParameterMap();
        Map<String,String> signParam=new HashMap<>();
        Iterator<String> iterator=callbackParam.keySet().iterator();
        while(iterator.hasNext()){
            String key=iterator.next();
            String[] values=callbackParam.get(key);
            StringBuffer buffer=new StringBuffer();
            if(values!=null&&values.length>0){
                for(int i=0;i<values.length;i++){
                    buffer.append(values[i]);
                    if(i!=values.length-1){
                        buffer.append(",");
                    }
                }
            }
            signParam.put(key,buffer.toString());
        }
        //验证签名
        try {
            signParam.remove("sign_type");
            boolean result= AlipaySignature.rsaCheckV2(signParam, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(result){
                //通过
                return orderService.callback(signParam);
            }
            else{
                return "fail";
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return "success";
    }
}
