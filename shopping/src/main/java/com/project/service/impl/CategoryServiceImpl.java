package com.project.service.impl;

import com.google.common.collect.Sets;
import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.dao.CategoryMapper;
import com.project.pojo.Category;
import com.project.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(Category category) {
        if(category==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        int result=categoryMapper.insert(category);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"添加品类失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse updateCategory(Category category) {
        if(category==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }
        if(category.getId()==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"类别id必传");
        }
        System.out.println(category.getImage());
        int result=categoryMapper.updateByPrimaryKey(category);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"更新品类失败");
        }
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse getCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"id必传");
        }
        List<Category> categoryList=categoryMapper.selectCategoryById(categoryId);
        return ServerResponse.serverResponseBySuccess(categoryList,"成功");
    }

    @Override
    public ServerResponse deepCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"id必传");
        }
        Set<Category> categorySet= Sets.newHashSet();
        Set<Category> categorySet1=findAllChildCategory(categorySet,categoryId);
        Set<Integer> categoryIds=Sets.newHashSet();
        Iterator<Category> iterator=categorySet1.iterator();
        while(iterator.hasNext()){
            Category category=iterator.next();
            categoryIds.add(category.getId());
        }
        Map map=new HashMap();
        map.values();
        return ServerResponse.serverResponseBySuccess(categoryIds);
    }

    @Override
    public ServerResponse<Category> selectCategory(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"id必传");
        }
        return ServerResponse.serverResponseBySuccess(categoryMapper.selectByPrimaryKey(categoryId));
    }

    @Override
    public ServerResponse<Category> showCategory() {
        List<Category> categoryList=categoryMapper.showCategory();
        if(categoryList!=null&&categoryList.size()>0){
            return ServerResponse.serverResponseBySuccess(categoryList);
        }
        return ServerResponse.serverResponseByError();
    }


    public Set<Category> findAllChildCategory(Set<Category> categorySet,Integer categoryId){

        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查看categoryId的平级子类
        List<Category> categoryList=categoryMapper.selectCategoryById(categoryId);
        if(categoryList!=null&&categoryList.size()>0){
            for (Category category1:categoryList){
                findAllChildCategory(categorySet,category1.getId());
            }
        }
        return categorySet;
    }
}
