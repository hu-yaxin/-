package com.project.service;

import com.project.common.ServerResponse;
import com.project.pojo.Shipping;


public interface IShippingService {

    /**
     * 添加地址
     * @param shipping
     * @return
     */
    public ServerResponse add(Shipping shipping);

    /**
     * 删除地址
     * @param shippingId
     * @return
     */
    public ServerResponse del(Integer shippingId);
    /**
     * 修改地址
     * @return
     */
    public ServerResponse update(Shipping shipping);
    /**
     * 查看地址
     * @return
     */
    public ServerResponse select(Integer shippingId);
    /**
     * 查看所有地址
     * @return
     */
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);


    public ServerResponse findShippingById(Integer shippingId);
}
