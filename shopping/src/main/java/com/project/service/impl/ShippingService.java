package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.dao.ShippingMapper;
import com.project.pojo.Shipping;
import com.project.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingService implements IShippingService {
    @Autowired
    ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Shipping shipping) {
        //参数非空校验
        if(shipping==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数必传");
        }
        Integer shippingId=shipping.getId();
        if (shippingId==null){
            //添加
            int result=shippingMapper.insert(shipping);
            if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加地址失败");
            }else{
                return ServerResponse.serverResponseBySuccess(shipping.getId());
            }
        }else{

        }

        return null;
    }

    @Override
    public ServerResponse del(Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数必传");
        }
        int result=shippingMapper.deleteByPrimaryKey(shippingId);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"删除失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }
    @Override
    public ServerResponse update(Shipping shipping) {
        if(shipping==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数必传");
        }
        if(shipping.getId()==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"id必传");
        }
        int result=shippingMapper.update(shipping);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse select(Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"id必传");
        }
        Shipping shipping=shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"地址不存在");
        }
        return ServerResponse.serverResponseBySuccess(shipping);
    }

    @Override
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize) {
        Page page= PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings=shippingMapper.findAllByUserid(userId);
        if(shippings==null||shippings.size()==0){
            return ServerResponse.serverResponseBySuccess();
        }
        PageInfo info=new PageInfo(page);
        return ServerResponse.serverResponseBySuccess(info);
    }

    @Override
    public ServerResponse findShippingById(Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"id必传");
        }

        Shipping shipping=shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"收货地址不存在");
        }
        return ServerResponse.serverResponseBySuccess(shipping);
    }
}
