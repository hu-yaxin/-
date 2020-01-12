package com.project.service.impl;

import com.google.common.collect.Lists;
import com.project.common.CheckEnum;
import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.dao.CartMapper;
import com.project.pojo.Cart;
import com.project.pojo.Product;
import com.project.service.ICartService;
import com.project.service.IProductService;
import com.project.util.BigDecimalUtils;
import com.project.vo.CartProductVO;
import com.project.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    IProductService productService;
    @Value("${business.imageHost}")
    private String imageHost;
    @Override
    public ServerResponse addProductToCart(Integer userId, Integer productId, Integer count) {
        //非空判断
        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id必传");
        }
        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品数量不能为空");
        }
        //判断商品是否存在
        ServerResponse<Product> serverResponse=productService.findProductById(productId);

        if(!serverResponse.isSuccess()){
            return ServerResponse.serverResponseByError(serverResponse.getStatus(),serverResponse.getMsg());
        }else{
            Product product=serverResponse.getData();
            if(product.getStock()<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品已售空");
            }
        }
        //判断商品是否在购物车中
        Cart cart=cartMapper.findCartByUserIdAndProductId(userId, productId);
        if(cart==null){
            Cart cart1=new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(CheckEnum.CART_PRODUCT_CHECK.getCheck());
            int result=cartMapper.insert(cart1);
            if(result<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加失败");
            }
        }else{
            //更新数量
            cart.setQuantity(cart.getQuantity()+count);
            int result1=cartMapper.updateByPrimaryKey(cart);
            if(result1<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新失败");
            }
        }
        CartVO cartVO=getCartVo(userId);


        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse findCartByUserid(Integer userId) {


        return ServerResponse.serverResponseBySuccess(getCartVo(userId));
    }

    @Override
    public ServerResponse updateCartByUseridAndProductid(Integer userId, Integer productId, Integer count,Integer checked) {
        if (productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品id必传");
        }
        Cart cart=cartMapper.findCartByUserIdAndProductId(userId,productId);
        if(cart==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品不存在");
        }
        if (count!=null) {
            if (count == 0) {
                int result = cartMapper.deleteByPrimaryKey(cart.getId());
                if (result <= 0) {
                    return ServerResponse.serverResponseByError(ResponseCode.ERROR, "删除失败");
                }
            }
            int result1=cartMapper.updateQuantityOrCheckedById(cart.getId(),count,checked);
            if(result1<0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
            }
        }
        int result=cartMapper.updateQuantityOrCheckedById(cart.getId(),count,checked);
        if(result<0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }
        CartVO cartVO=getCartVo(userId);

        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse select_all(Integer userId) {
        List<Cart> carts=cartMapper.findCartsByUserid(userId);
        if(carts==null||carts.size()==0){
            return ServerResponse.serverResponseBySuccess();
        }
        for (Cart cart:carts){
            if (cart.getChecked()==CheckEnum.CART_PRODUCT_UNCHECK.getCheck()){
                cart.setChecked(CheckEnum.CART_PRODUCT_CHECK.getCheck());
                int result=cartMapper.updateByPrimaryKey(cart);
                if (result<=0){
                    return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
                }
            }
        }

        return ServerResponse.serverResponseBySuccess(getCartVo(userId));
    }

    @Override
    public ServerResponse un_select_all(Integer userId) {
        List<Cart> carts=cartMapper.findCartsByUserid(userId);
        if(carts==null||carts.size()==0){
            return ServerResponse.serverResponseBySuccess();
        }
        for (Cart cart:carts){
            if (cart.getChecked()==CheckEnum.CART_PRODUCT_CHECK.getCheck()){
                cart.setChecked(CheckEnum.CART_PRODUCT_UNCHECK.getCheck());
                int result=cartMapper.updateByPrimaryKey(cart);
                if (result<=0){
                    return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
                }
            }
        }

        return ServerResponse.serverResponseBySuccess(getCartVo(userId));
    }

    @Override
    public ServerResponse<List<Cart>> findCartsByUserIdAndChecked(Integer userId) {

        List<Cart> carts=cartMapper.findCartsByUseridAndChecked(userId);
        return ServerResponse.serverResponseBySuccess(carts);
    }

    @Override
    public ServerResponse deleteBath(List<Cart> carts) {

        if(carts==null||carts.size()==0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"要删除的购物车商品不能为空");
        }
        int result=cartMapper.deleteBath(carts);
        if(result!=carts.size()){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"购物车清空失败");
        }

        return ServerResponse.serverResponseBySuccess();
    }


    private CartVO getCartVo(Integer userId){
        CartVO cartVO=new CartVO();

        //step1:根据userid查询该用户的购物信息 -->List<Cart>

        List<Cart> cartList= cartMapper.findCartsByUserid(userId);

        if(cartList==null||cartList.size()==0){
            return  cartVO;
        }

        //定义购物车商品总价格
        BigDecimal cartTotalPrice=new BigDecimal("0");
        //step2: List<Cart> --> List<CartProductVO>

        List<CartProductVO> cartProductVOList= Lists.newArrayList();
        int limit_quantity=0;
        String limitQuantity=null;
        for(Cart cart:cartList){
            //Cart-->CartProductVO
            CartProductVO cartProductVO=new CartProductVO();
            cartProductVO.setId(cart.getId());
            cartProductVO.setUserId(userId);
            cartProductVO.setProductId(cart.getProductId());

            ServerResponse<Product> serverResponse=productService.findProductById(cart.getProductId());
            if(serverResponse.isSuccess()){
                Product product=serverResponse.getData();
                if(product.getStock()>=cart.getQuantity()){
                    limit_quantity=cart.getQuantity();
                    limitQuantity="LIMIT_NUM_SUCCESS";
                }else{
                    limit_quantity=product.getStock();
                    limitQuantity="LIMIT_NUM_FAIL";
                }
                cartProductVO.setQuantity(limit_quantity);
                cartProductVO.setLimitQuantity(limitQuantity);
                cartProductVO.setProductName(product.getName());
                cartProductVO.setProductSubtitle(product.getSubtitle());
                cartProductVO.setProductMainImage(imageHost+product.getMainImage());
                cartProductVO.setProductPrice(product.getPrice());
                cartProductVO.setProductStatus(product.getStatus());
                cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),
                        cart.getQuantity()*1.0));
                cartProductVO.setProductStock(product.getStock());
                cartProductVO.setProductChecked(cart.getChecked());
                cartProductVOList.add(cartProductVO);

                if(cart.getChecked()==CheckEnum.CART_PRODUCT_CHECK.getCheck()){
                    //商品被选中
                    cartTotalPrice=BigDecimalUtils.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }

            }

        }

        cartVO.setCartProductVOList(cartProductVOList);

        //step3:计算购物车总得价格
        cartVO.setCarttotalprice(cartTotalPrice);

        //step4:判断是否全选
        Integer isAllChecked=  cartMapper.isAllChecked(userId);

        if(isAllChecked==0){
            //全选
            cartVO.setIsallchecked(true);
        }else {
            cartVO.setIsallchecked(false);
        }
        //step5:构建cartvo




        return cartVO;

    }
}
