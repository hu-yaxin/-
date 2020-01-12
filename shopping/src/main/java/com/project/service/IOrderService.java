package com.project.service;

import com.project.common.ServerResponse;
import com.project.pojo.Order_item;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface IOrderService {

    public ServerResponse createOrder(Integer userId,Integer shippingId);
    /**
     * 获取用户订单商品详情
     */
    public ServerResponse get_order_cart_product(Integer userId);
    /**
     *订单list
     */
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);

    /**
     * 管理员查询所有订单
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse AllList(Integer pageNum,Integer pageSize);

    /**
     *通过订单编号查询订单
     * @param orderNo
     * @return
     */
    public ServerResponse detail(Long orderNo);

    /**
     * 取消订单
     * @param orderNo
     * @return
     */
    public ServerResponse cancel(Long orderNo);
    /**
     * 支付
     */
    public ServerResponse pay(Long orderNo);
    /**
     * 支付回调接口
     */
    public String callback(Map<String,String> requestParams);

    /**
     * 修改订单状态
     * @param orderNo
     * @param status
     * @return
     */
    public ServerResponse setStatus(Long orderNo,Integer status);

    /**
     * 通过订单号查询订单
     * @param orderNo
     * @return
     */
    public ServerResponse getOrder(Long orderNo);


}
