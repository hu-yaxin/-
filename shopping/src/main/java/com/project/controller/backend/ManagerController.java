package com.project.controller.backend;


import com.project.common.ResponseCode;
import com.project.common.ServerResponse;
import com.project.pojo.User;
import com.project.service.impl.UserServiceImpl;
import com.project.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manager/")
public class ManagerController {
    @Autowired
    UserServiceImpl userService;
    /**
     * 登录接口
     */
    @RequestMapping(value = "login.do/{username}/{password}")
    public ServerResponse login(@PathVariable("username") String username,
                                @PathVariable("password") String password,
                                HttpSession session){
        ServerResponse serverResponse=userService.login(username, password,0);
        if (serverResponse.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,serverResponse.getData());
        }
        return  serverResponse;
    }
    @RequestMapping(value = "logout.do")
    public ServerResponse logout(User user,HttpSession session){

        session.removeAttribute(Const.CURRENT_USER);
        if(session.getAttribute(Const.CURRENT_USER)!=null){
            return ServerResponse.serverResponseByError(ResponseCode.ERROR,"服务端异常");
        }
        return  ServerResponse.serverResponseBySuccess("退出成功");
    }


}
