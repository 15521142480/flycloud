package com.fly.file.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.fly.common.utils.Base64Utils;
import com.fly.common.utils.DateUtils;
import com.fly.common.utils.StringUtils;
import com.fly.file.admin.domain.bo.UserBo;
import com.fly.file.admin.singleton.ConFtpSingle;
import com.fly.file.admin.singleton.UserSingle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录模块
 *
 * @author lxs
 * @date 2023/4/24
 */
@RestController("loginController")
@RequestMapping("/")
public class LoginController {


    private final static int timeout = 7200;


    /**
     * pc登录
     *
     * @param base64 账号和密码合并后的base64
     */
    @PostMapping(value = "/login")
    public Object login(String base64, HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // base64顺序前端已打乱, 后四位移动到了最前面
        if (StringUtils.isBlank(base64)) {
            resultMap.put("resultCode", "10001");
            resultMap.put("resultMsg", "账号或密码不能为空！");
            return resultMap;
        }

        try {

            String newBase64 = base64.substring(4) + base64.substring(0, 4);
            String newBase = Base64Utils.decode64(newBase64); // 账号#密码
            String loginName = newBase.split("#")[0];
            String password = newBase.split("#")[1];

            // 账号/密码生成
            String autoLoginName = "admin";
            String autoPassword = "admin";

//            int curWeek = DateUtils.getCurWeekOfDate(new Date()); // 当前周几
//            int curMonth = DateUtils.getCurDateNum("MM"); // 当前几月 (去零处理 如10 -> 1)
            int curDay = DateUtils.getCurDateNum("dd"); // 当前几号 (去零处理 如30 -> 3)
//            String lastPasswordNum = String.valueOf(curWeek * curMonth * curDay);

//            autoLoginName += String.valueOf(curWeek);
//            autoPassword += lastPasswordNum;

            autoPassword += curDay;

            if (!loginName.equals(autoLoginName) || !password.equals(autoPassword)) {
                resultMap.put("resultCode", "10002");
                resultMap.put("resultMsg", "账号或密码错误！");
                return resultMap;
            }

            // token
            String userToken = StringUtils.genIndexCode(32);

            // 用户信息
            Map<String, Object> userInfo = new HashMap<>();
            String userId = "0001";
            userInfo.put("userId", userId);
            userInfo.put("userName", loginName);
            userInfo.put("userToken", userToken);

            // 用户单例
            UserBo userBean = new UserBo();
            userBean.setUserId(userId);
            userBean.setUserToken(userToken);
            UserSingle userSingle = UserSingle.getUserSingleInst();
            userSingle.addUser(userBean);

            // 向客户端设置cookie
//            String userInfoStr = JSONObject.toJSONString(userBean); // cookie不能有[ ] ( ) = , " / ? @ : ;字符  所以不能存放此信息
//            CookieUtils.setCookie(request, response, "userToken", userToken, timeout);

            resultMap.put("data", userInfo);
            resultMap.put("resultMsg", "登录成功！");
            resultMap.put("resultCode", "1");

        } catch (Exception e) {
            resultMap.put("resultMsg", "登录失败！");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 登出
     *
     */
    @PostMapping(value = "/loginOut")
    public Object loginOut(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {

            String requestUserToken = request.getHeader("userToken");

            if (StringUtils.isBlank(requestUserToken)) {
                resultMap.put("resultCode", "1");
                resultMap.put("resultMsg", "登出系统成功(token为空)!");
                return resultMap;
            }

            // 用户单例
            UserSingle userSingle = UserSingle.getUserSingleInst();
            UserBo user = userSingle.getUserByToken(requestUserToken);
            if (user != null) {
                userSingle.removeUserById(user.getUserId());
            }

            // cookie
            // CookieUtils.removeCookie(request, response, "userToken");

            // todo 登出ftp
            try {
                ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
                singleton.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            resultMap.put("resultCode", "1");
            resultMap.put("resultMsg", "登出系统成功!");

        } catch (Exception e) {

            resultMap.put("resultCode", "10000");
            resultMap.put("resultMsg", "系统出错");
        }

        return resultMap;
    }

}
