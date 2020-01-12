package com.project.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.dao.ProductMapper;
import com.project.pojo.Category;
import com.project.pojo.Product;
import com.project.service.ICategoryService;
import com.project.service.IProductService;
import com.project.util.DateUtils;
import com.project.util.FTPUtil;
import com.project.util.PropertiesUtils;
import com.project.vo.ProductDetailVO;
import com.project.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ICategoryService categoryService;

    @Value("${business.imageHost}")
    private String imageHost;

    @Override
    public ServerResponse addOrUpdate(Product product) {
        if(product==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        /**
         * subimges
         */
        String subImages=product.getSubImages();
        if(subImages!=null&&!subImages.equals("")){
            String[] subImageArr=subImages.split(",");
            if(subImages.length()>0){
                //设置商品主图
                product.setMainImage(subImageArr[0]);
            }
        }



        Integer productId=product.getId();
        if(productId==null){
            int result=productMapper.insert(product);
            //商品添加
            if(result<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"插入商品失败");
            }else{
                return ServerResponse.serverResponseBySuccess();

            }
        }else{
            //商品更新
            int result1=productMapper.updateByPrimaryKey(product);
            if(result1<=0){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改商品失败");
            }else{
                return ServerResponse.serverResponseBySuccess();
            }

        }
    }

    /**
     * 修改产品状态
     * @param id
     * @param status
     * @return
     */
    @Override
    public ServerResponse setSaleStatus(Integer id, Integer status) {
        if(id==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"商品id必传");
        }
        if(status==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"状态参数为空");
        }
            int result=productMapper.setSaleStatus(id,status);
        if(result==0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }

        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse upload(MultipartFile file, String path) {
        if(file==null){
            return ServerResponse.serverResponseByError();
        }

        //step1:获取图片名称
        String  orignalFileName=  file.getOriginalFilename();
        //获取图片的扩展名
        String exName=  orignalFileName.substring(orignalFileName.lastIndexOf(".")); // .jpg
        //为图片生成新的唯一的名字
        String newFileName= UUID.randomUUID().toString()+exName;

        File pathFile=new File(path);
        if(!pathFile.exists()){
            pathFile.setWritable(true);
            pathFile.mkdirs();
        }

        File file1=new File(path,newFileName);

        try {
            file.transferTo(file1);
            //上传到图片服务器
            FTPUtil.uploadFile(Lists.newArrayList(file1));
            //.....
            Map<String,String> map= Maps.newHashMap();
            map.put("uri",newFileName);
            map.put("url", PropertiesUtils.readByKey("imageHost")+"/"+newFileName);

            //删除应用服务器上的图片
            file1.delete();

            return ServerResponse.serverResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public ServerResponse search(String productName, Integer productId, Integer pageNum, Integer pageSize) {

        if(productName!=null){
            productName="%"+productName+"%";
        }
        Page page= PageHelper.startPage(pageNum,pageSize);
        List<Product> productList=productMapper.findProductByNameAndId(productId,productName);

        List<ProductDetailVO>productDetailVOS= Lists.newArrayList();
            if(productList!=null&&productList.size()>0){
                for (Product product:productList){
                    ProductDetailVO productDetailVO =assembleProductDetailVO(product);
                    productDetailVOS.add(productDetailVO);
                }
        }
        PageInfo info=new PageInfo(page);
        info.setList(productDetailVOS);
        return ServerResponse.serverResponseBySuccess(info);
    }

    @Override
    public ServerResponse<ProductDetailVO> detail(Integer productId) {

        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"商品id必传");
        }
        Product product =productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.serverResponseBySuccess();
        }
        ProductDetailVO productDetailVO=assembleProductDetailVO(product);
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<Product> findProductById(Integer productId) {
        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"商品id必传");
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"商品不存在");

        }

        return ServerResponse.serverResponseBySuccess(product);
    }

    @Override
    public ServerResponse reduceStock(Integer productId, Integer stock) {
        if(productId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"商品id必传");
        }

        if(stock==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"库存参数必传");
        }
        int result=productMapper.reduceProductStock(productId,stock);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"扣除库存失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse<ProductDetailVO> showHot() {
        List<Product> productList=productMapper.findHotProduct();
        List<ProductDetailVO> detailVOS=Lists.newArrayList();
        if(productList!=null && productList.size()>0){
            for (Product product:productList){
                detailVOS.add(assembleProductDetailVO(product));
            }
        }
        return ServerResponse.serverResponseBySuccess(detailVOS);
    }

    private ProductDetailVO assembleProductDetailVO(Product product){


        ProductDetailVO productDetailVO=new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setImageHost(imageHost);
        productDetailVO.setName(product.getName());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setId(product.getId());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
            productDetailVO.setNew(product.getIsNew());
            productDetailVO.setHot(product.getIsHot());
        ServerResponse<Category> categoryServerResponse= categoryService.selectCategory(product.getCategoryId());
        Category category=categoryServerResponse.getData();
        if(category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        return productDetailVO;
    }

    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO=new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(imageHost+product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        if(product.getIsNew()==true){
            productListVO.setIsNew(1);
        }else{
            productListVO.setIsNew(0);
        }
        if(product.getIsHot()==true){
            productListVO.setIsHost(1);
        }else{
            productListVO.setIsHost(0);
        }
        return  productListVO;
    }

}

