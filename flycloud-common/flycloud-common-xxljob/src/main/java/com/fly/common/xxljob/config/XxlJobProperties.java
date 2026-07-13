package com.fly.common.xxljob.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * XXL-JOB 执行器配置。
 *
 * <p>仅当 {@code xxl.job.executor.enabled=true} 时才创建执行器。</p>
 *
 * @author lxs
 * @date 2026-07-13
 */
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

    private final Admin admin = new Admin();

    private final Executor executor = new Executor();

    public Admin getAdmin() {
        return admin;
    }

    public Executor getExecutor() {
        return executor;
    }

    /**
     * 调度中心连接配置。
     */
    public static class Admin {

        private String addresses;

        private String accessToken;

        private int timeout = 3;

        public String getAddresses() {
            return addresses;
        }

        public void setAddresses(String addresses) {
            this.addresses = addresses;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }

    /**
     * 当前业务服务的执行器配置。
     */
    public static class Executor {

        private boolean enabled;

        private String appname;

        private String address;

        private String ip;

        private int port = 9999;

        private String logPath = "/data/applogs/xxl-job";

        private int logRetentionDays = 30;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getLogPath() {
            return logPath;
        }

        public void setLogPath(String logPath) {
            this.logPath = logPath;
        }

        public int getLogRetentionDays() {
            return logRetentionDays;
        }

        public void setLogRetentionDays(int logRetentionDays) {
            this.logRetentionDays = logRetentionDays;
        }
    }
}
