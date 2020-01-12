package com.project.service.impl;

import com.project.common.ResponseCode;
import com.project.common.RoleEnum;
import com.project.common.ServerResponse;
import com.project.dao.UserMapper;
import com.project.pojo.User;
import com.project.service.IUserService;
import com.project.util.MD5Utils;
import com.project.util.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse register(User user) {
        //1.参数校验
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"参数不能为空");
        }

        //2.判断用户是否存在
        int result =userMapper.isExistsUserName(user.getUsername());
        if(result>0){
            return ServerResponse.serverResponseByError(ResponseCode.USERNAME_EXISTS,"用户名已存在");
        }
        //3.判断邮箱是否存在
        int resultEmai =userMapper.isExistsEmail(user.getEmail());
        if(resultEmai>0){
            return ServerResponse.serverResponseByError(ResponseCode.EMAIL_EXISTS,"邮箱已存在");
        }
        //4.密码加密，设置用户角色
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        //设置角色为普通用户
        user.setRole(RoleEnum.ROLE_USER.getRole());
        //5.注册
        int insertResult=userMapper.insert(user);
        if(insertResult<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"注册失败");
        }
        //6.返回
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse login(String username, String password,int type) {
        //1.参数校验
        if(username==null||username.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        }
        if(password==null||password.equals("")){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码不能为空");
        }
        //2.判断用户是否存在
        int result=userMapper.isExistsUserName(username);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.USERNAME_EXISTS,"用户名不存在");
        }
        //3.为密码加密
        password=MD5Utils.getMD5Code(password);
        //4.判断用户名密码是否正确
        User user=userMapper.findUserByUsernameAndPassword(username,password);
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码错误");
        }
        if(type==0){
            if(user.getRole()==RoleEnum.ROLE_USER.getRole()){
                return ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
            }
        }
        return ServerResponse.serverResponseBySuccess(user);
    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //1.参数非空校验
        if(username==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        }
        //2.通过用户名获得密保问题
        String question=userMapper.forget_get_question(username);
        if(question==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"用户没有设置密保问题");
        }
        //3.返回、

        return ServerResponse.serverResponseBySuccess(question);
    }

    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //1.参数非空校验
        if(username==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        }
        if(question==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"问题不能为空");
        }
        if(answer==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案不能为空");
        }
        //2.判断答案是否正确
        int result =userMapper.forget_check_answer(username,question,answer);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"答案错误");
        }
        //3.返回、
        //生成token
        String token=UUID.randomUUID().toString();
        TokenCache.set("username"+username,token );
        return ServerResponse.serverResponseBySuccess(token);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String forgetToken) {
        //1.参数非空校验
        if(username==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"用户名不能为空");
        }
        if(passwordNew==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码不能为空");
        }
        if(forgetToken==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"token不能为空");
        }
        //2.修改密码
        String token=TokenCache.get("username"+username);
        if(token==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"不能修改别人的密码或者token已经过期");
        }
        if(!token.equals(forgetToken)){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"无效的token");
        }

        int result =userMapper.forget_reset_password(username, MD5Utils.getMD5Code(passwordNew));
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改密码失败");
        }
        //3.返回、
        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse updateUserByActive(User user) {
        //非空判断
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"参数不能为空");
        }
        user.setPassword(MD5Utils.getMD5Code(user.getPassword()));
        int result=userMapper.updateUserByActive(user);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }

        return ServerResponse.serverResponseBySuccess();
    }

    @Override
    public ServerResponse reset_password(String username, String passwordOld, String passwordNew) {
        //1.参数非空校验
        if(passwordOld==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"旧密码不能为空");
        }
        if(passwordNew==null){
            return ServerResponse.serverResponseByError(ResponseCode.PARAM_NOT_NULL,"新密码不能为空");
        }
        //2.判断密码是否正确
        User user=userMapper.findUserByUsernameAndPassword(username,MD5Utils.getMD5Code(passwordOld));
        if(user==null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"密码错误");
        }

        //3.设置新密码
        user.setPassword(MD5Utils.getMD5Code(passwordNew));
        int result=userMapper.updateUserByActive(user);
        if(result<=0){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"修改失败");
        }

        return ServerResponse.serverResponseBySuccess();
    }


}
