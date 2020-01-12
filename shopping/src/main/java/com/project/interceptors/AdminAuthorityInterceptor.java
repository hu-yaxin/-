package com.project.interceptors;


import com.project.common.ResponseCode;
import com.project.common.RoleEnum;
import com.project.common.ServerResponse;
import com.project.pojo.User;
import com.project.util.Const;
import com.project.util.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

@Component
public class AdminAuthorityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("==========adminInterceptor");
        HttpSession session=request.getSession();
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            response.reset();
            PrintWriter printWriter=response.getWriter();
            ServerResponse serverResponse =ServerResponse.serverResponseByError(ResponseCode.NOT_LOGIN,"未登录");
            String json=JsonUtils.obj2String(serverResponse);
            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
            return false;
        }
        int role=user.getRole();
        if(role== RoleEnum.ROLE_USER.getRole()){

            response.reset();
            PrintWriter printWriter=response.getWriter();
            ServerResponse serverResponse= ServerResponse.serverResponseByError(ResponseCode.ERROR,"权限不足");
            String json=JsonUtils.obj2String(serverResponse);
            response.setHeader("Contex-Type","application/json;charset=UTF-8");
            printWriter.write(json);
            printWriter.flush();
            printWriter.close();
            System.out.println(false);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
