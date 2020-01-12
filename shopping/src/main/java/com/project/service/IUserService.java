package com.project.service;

import com.project.common.ServerResponse;
import com.project.pojo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public interface IUserService {
        /**
         * 注册接口
         */
        public ServerResponse register(User user);
        /**
         * 登录接口
         */
        public ServerResponse login(String username,String password,int type);

        /**
         * 根据username查询密保问题
         */

        public ServerResponse forget_get_question(@PathVariable("username") String username);
        /**
         * 提交问题答案
         */
        public ServerResponse forget_check_answer(String username, String question,String answer );
        /**
         * 修改密码
         */
        public ServerResponse forget_reset_password(String username, String passwordNew,String forgetToken );
        /**
         * 登录修改用户信息
         */
        public ServerResponse updateUserByActive(User user);
        /**
         * 登录修改密码
         */
        public ServerResponse reset_password(String username,String passwordOld,String passwordNew);




}
