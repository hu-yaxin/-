package com.project.service;

import com.project.common.ServerResponse;
import com.project.pojo.Category;
import com.project.pojo.Product;
import com.project.vo.ProductDetailVO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


public interface IProductService {

    public ServerResponse addOrUpdate(Product product);

    /**
     * 修改商品状态
     */
    public ServerResponse setSaleStatus(Integer id,Integer status);
    /**
     * 图片上传
     * */
    ServerResponse upload(MultipartFile file, String path);

    /**
     * 商品查询
     * @param productName
     * @param productId
     * @param pageSize
     * @return
     */
    public ServerResponse search(String productName, Integer productId,Integer pageNum,Integer pageSize);

    /**
     * 商品详情查询
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVO> detail(Integer productId);

    /**
     * 根据商品Id查询商品信息（库存）
     */
    public ServerResponse<Product> findProductById(Integer productId);

    /**
     * 扣库存
     */
    public ServerResponse reduceStock(Integer productId,Integer stock);

    /**
     * 查询热销商品
     *
     * @return
     */
    public ServerResponse<ProductDetailVO> showHot();





}
