package com.project.controller.front;


import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.pojo.User;
import com.project.service.impl.UserServiceImpl;
import com.project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
@RestController
@RequestMapping("/user/")
public class UserController {
    @Autowired
    UserServiceImpl userService;
    /**
     * 注册接口
     */
    @RequestMapping(value = "register.do")
    public ServerResponse register( User user){
        return userService.register(user);
    }

    /**
     * 登录接口
     */
    @RequestMapping(value = "login.do/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username,
                                @PathVariable("password") String password,
                                HttpSession session){
        ServerResponse serverResponse=userService.login(username, password,1);
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return  serverResponse;
    }
    /**
     * 根据username查询密保问题
     */

    @RequestMapping(value = "forget_get_question.do/{username}")
    public ServerResponse forget_get_question(@PathVariable("username") String username){
        return  userService.forget_get_question(username);
    }
    /**
     * 提交问题答案
     */
    @RequestMapping(value = "forget_check_answer.do")
    public ServerResponse forget_check_answer(String username, String question,String answer ){
        return  userService.forget_check_answer(username, question, answer);
    }
    /**
     * 修改密码
     */
    @RequestMapping(value = "forget_reset_password.do")
    public ServerResponse forget_reset_password(String username, String passwordNew,String forgetToken ){
        return  userService.forget_reset_password(username, passwordNew, forgetToken);
    }
    /**
     * 登录状态修改用户信息
     */
    @RequestMapping(value = "update_information.do")
    public ServerResponse update_information(User user ,HttpSession session){
        User loginUser=(User)session.getAttribute(Const.CURRENT_USER);
        user.setId(loginUser.getId());
        return  userService.updateUserByActive(user);
    }
    /**
     * 退出登录
     */
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        if(session.getAttribute(Const.CURRENT_USER)!=null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"服务端异常");
        }
        return  ServerResponse.serverResponseBySuccess("退出成功");
    }
    /**
     * 登录的用户信息
     */
    @RequestMapping(value = "get_user_info.do")
    public ServerResponse get_user_info(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);

        return  ServerResponse.serverResponseBySuccess(user);
    }

    /**
     * 登录状态重置密码
     */
        @RequestMapping(value = "reset_password.do")
    public ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        return  userService.reset_password(user.getUsername(),passwordOld,passwordNew);
    }
}
