package com.fly.file.admin.config;

/**
 * SettleLogger日志
 *
 * @author lxs
 * @date 2023/2/2
 */
public class SettleLogger implements com.jcraft.jsch.Logger{

    public boolean isEnabled(int level) {

        return true;

    }

    public void log(int level, String msg) {

        System.out.println(msg);

    }
}
