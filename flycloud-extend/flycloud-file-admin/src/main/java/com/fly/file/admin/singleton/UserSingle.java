package com.fly.file.admin.singleton;

import com.fly.common.utils.StringUtils;
import com.fly.file.admin.domain.bo.UserBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * user信息 (单例)
 *
 * @author lxs
 * @date 2023/4/24
 */
public class UserSingle {

    private static Logger log = LoggerFactory.getLogger(UserSingle.class);

    // 用户信息
    private static List<UserBo> userList;

    /**
     * 初始构造
     */
    private UserSingle(){
        log.info("================================ 创建一个 用户信息 懒加载模式且线程安全的单例 ");
    }


    // ===================================================== 处理单例
    /**
     * 静态内部类实现模式（线程安全，调用效率高，可以延时加载)
     * 用来记录感知是否进行实例化
     */
    private static class UserTokenSingleInst{
        private static UserSingle singleton = new UserSingle();
    }

    /**
     * 获取ftp连接单例
     * @return
     */
    public static UserSingle getUserSingleInst(){
        return UserTokenSingleInst.singleton;
    }


    /**
     * 添加用户
     */
    public void addUser(UserBo userBean) {

        if (userList == null) {
            userList = new ArrayList<>();
            userList.add(userBean);
        } else {

            boolean isExistUser = false;
            int isExistUserIndex = 0;

            for (UserBo user : userList) {
                if (user.getUserId().equals(userBean.getUserId())) {
                    isExistUser = true;
                    break;
                }
                isExistUserIndex ++;
            }

            if (isExistUser) {
                userList.add(isExistUserIndex, userBean);
            } else {
                userList.add(userBean);
            }
        }
    }

    /**
     * 获取用户id获取用户信息
     */
    public UserBo getUserById(String userId) {

        if (StringUtils.isBlank(userId) || userList == null) {
            return null;
        }
        for (UserBo user : userList) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 获取用户userToken获取用户信息
     */
    public UserBo getUserByToken(String userToken) {

        if (StringUtils.isBlank(userToken) || userList == null) {
            return null;
        }
        for (UserBo user : userList) {
            if (user.getUserToken().equals(userToken)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 删除用户信息
     */
    public void removeUserById(String userId) {

        if (StringUtils.isBlank(userId) || userList == null) {
            return;
        }
        int userIndex = 0;
        for (UserBo user : userList) {
            if (user.getUserId().equals(userId)) {
                userList.remove(userIndex);
                return;
            }
            userIndex ++;
        }
    }


}
