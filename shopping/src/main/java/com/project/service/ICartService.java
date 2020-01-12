package com.project.service;

import com.project.common.ServerResponse;
import com.project.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICartService {

    /**
     * 添加商品到购物车
     */
    public ServerResponse addProductToCart(Integer userId,Integer productId,Integer count);
    /**
     * 查询购物车列表
     */
    public ServerResponse findCartByUserid(Integer userId);
    /**
     * 更新购物车某个商品数量(移除，改变数量，选中，取消选中)
     */
    public ServerResponse updateCartByUseridAndProductid(Integer userId,Integer productId,Integer count,Integer checkd);

    /**
     * 全选
     * @param userId
     * @return
     */
    public ServerResponse select_all(Integer userId);
    /**
     * 取消全选
     * @param userId
     * @return
     */
    public ServerResponse un_select_all(Integer userId);

    /**
     * 根据用户id查看已选中的商品
     */
    public ServerResponse<List<Cart>> findCartsByUserIdAndChecked(Integer userId);
    /**
     * 批量删除购物车信息
     */
    public ServerResponse deleteBath(List<Cart> carts);
}
