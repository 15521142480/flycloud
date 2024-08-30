package com.fly.learn.thread;

import com.fly.learn.thread.entity.LoginUserInfo;

/**
 * ThreadLocal - 线程本地变量
 *
 * <p>
 * ThreadLocal 能够隔离线程间的数据共享，提供线程级别的数据存储。
 *
 * @author: lxs
 * @date: 2024/8/24
 */
public class ThreadLocalTest {


    /**
     * 以ThreadLocal形式定义变量；存储登录用户信息
     */
    public static final ThreadLocal<LoginUserInfo> currentUserThreadLocal = new ThreadLocal<>();


    // 设置值
    public static void setCurrentUser(LoginUserInfo user) {
        currentUserThreadLocal.set(user);
    }

    // 获取值
    public static LoginUserInfo getCurrentUser() {
        return currentUserThreadLocal.get();
    }

    // 删除值
    public static void clearCurrentUser() {
        currentUserThreadLocal.remove();
    }



    /**
     * 测试
     */
    public static void main(String[] args) {


        new Thread(() -> {

            LoginUserInfo loginUserInfo = new LoginUserInfo();
            loginUserInfo.setUserId("1");
            loginUserInfo.setUsername("用户1");
            currentUserThreadLocal.set(loginUserInfo);
            System.out.println("线程1登录系统，用户id: " + currentUserThreadLocal.get().getUserId());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 退出登录
            System.out.println("用户id：" + currentUserThreadLocal.get().getUserId() + "退出系统啦！");
            currentUserThreadLocal.remove();

        }).start();

        new Thread(() -> {

            LoginUserInfo loginUserInfo = new LoginUserInfo();
            loginUserInfo.setUserId("2");
            loginUserInfo.setUsername("用户2");
            currentUserThreadLocal.set(loginUserInfo);
            System.out.println("线程2登录系统，用户id: " + currentUserThreadLocal.get().getUserId());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // 退出登录
            System.out.println("用户id：" + currentUserThreadLocal.get().getUserId() + "退出系统啦！");
            currentUserThreadLocal.remove();

        }).start();
    }

}

