package com.fly.file.admin.singleton;


import com.fly.file.admin.config.SettleLogger;
import com.fly.file.admin.domain.bo.ConnectFtpBo;
import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/**
 * 连接的ftp的单例对象 (单例)
 *
 * @author lxs
 * @date 2023/4/24
 */
public class ConFtpSingle {

    private static Logger log = LoggerFactory.getLogger(ConFtpSingle.class);

    // 连接对象
    private static ChannelSftp sftp;

    private static Channel channel;

    private static ChannelExec channelExec;

//    //账号
//    private static String user;
//    //主机ip
//    private static String host;
//    //密码
//    private static String password;
//    //端口
//    private static int port;
//    //上传地址
//    private static String directory;
//    //下载目录
//    private static String saveFile;

    /**
     * 初始构造
     */
    private ConFtpSingle(){
        log.info("================================ 创建一个连接的ftp懒加载模式且线程安全的单例 ");
//        if (sftp == null) {
//            getConnect();
//        }
    }


    // ===================================================== 处理单例
    /**
     * 静态内部类实现模式（线程安全，调用效率高，可以延时加载)
     * 用来记录感知是否进行实例化
     */
    private static class SConFtpSingleInst{
        private static ConFtpSingle singleton = new ConFtpSingle();
    }

    /**
     * 获取ftp连接单例
     * @return
     */
    public static ConFtpSingle getConFtpSingle(){
        return SConFtpSingleInst.singleton;
    }


    // ============================================================ 远程连接 登陆 查询 命令 上传 删除
    /**
     * 连接ftp
     */
    public void loginConnect(ConnectFtpBo conBean) throws JSchException {

        com.jcraft.jsch.Logger logger = new SettleLogger();
        JSch.setLogger(logger);

        JSch jsch = new JSch();

        // 获取sshSession 账号-ip-端口
        Session sshSession = jsch.getSession(conBean.getUser(), conBean.getHost(), conBean.getPort());
        sshSession.setPassword(conBean.getPassword()); // 添加密码
        Properties sshConfig = new Properties();

        // 严格主机密钥检查
        sshConfig.put("StrictHostKeyChecking", "no");// do not verify host
        sshSession.setConfig(sshConfig);


        // 开启sshSession链接
        sshSession.connect();
//         sshSession.connect(300000); // 连接会话时间

        // 获取sftp通道 (sftp模式)
        Channel channel = sshSession.openChannel("sftp");

        // 开启
        channel.connect();
        sftp = (ChannelSftp) channel;
    }



    /**
     * 退出ftp连接
     */
    public void loginOutConnect() throws JSchException {

        sftp.exit();
    }


    /**
     * 切断ftp连接
     * @param
     * @throws Exception
     */
    public void disconnect() {
        if (sftp != null) {
            if (sftp.isConnected()) { // 是否正在连接中
                sftp.disconnect();
            } else if (sftp.isClosed()) { // 连接是否已经失效
                sftp.disconnect();
            }
        }
    }


    /**
     * 连接状态
     * @param
     * @throws Exception
     */
    public Map<String, Object> getConnect() {

        Map<String, Object> connectMap = new HashMap<>();
        if (sftp == null) {
            connectMap.put("resultCode", "2");
            connectMap.put("resultMsg", "ftp未登陆!");
        } else if ( sftp.isClosed()) { // 连接是否已经失效
            connectMap.put("resultCode", "3"); // 连接会话失效
            connectMap.put("resultMsg", "ftp连接会话失效!");
        } else {
            connectMap.put("resultCode", "1");
            connectMap.put("resultMsg", "ftp连接正常!");
        }

        return connectMap;
    }


    public boolean isConnect() {

        if (sftp == null) {
            return false;
        } else if ( sftp.isClosed()) { // 连接是否已经失效
            return false;
        }

        return true;
    }


    /**
     * 执行命令
     * @param cmd
     * @return
     * @throws Exception
     */
    public String executeCmd(String cmd) throws Exception {

        if (!isConnect()) {
            throw new JSchException("请先登陆远程链接!");
        }

        log.info("================================ 开始执行命令: " + cmd);
        String result = "";

        Channel channel = sftp.getSession().openChannel("exec"); // 命令模式
        ChannelExec channelExec = (ChannelExec) channel;

        channelExec.setCommand(cmd);
        channelExec.connect();

        InputStream in = channel.getInputStream();
        result = IOUtils.toString(in, "UTF-8");

        return result;
    }



    /**
     * @description: 获取channel
     * @author: lxs
     * @date: 2022/8/21 22:07
     * @param channelSftp
     * @return {@link Channel}
    */
    public Channel getChannel(ChannelSftp channelSftp) throws JSchException {

        if (channel != null) {
            return channel;
        } else {
            return channelSftp.getSession().openChannel("exec");
        }
    }


    /**
     * 上传单个文件
     * @param multipartFile 原文件
     * @param curPath 服务器当前上传的路径
     * @throws Exception
     */
    public String uploadFile(MultipartFile multipartFile, String curPath) throws Exception {

        if (sftp == null) {
            throw new JSchException("请先登陆远程链接!");
        }

        String fileName = curPath + File.separator +  multipartFile.getOriginalFilename(); // 上传到服务器的新文件路径
        sftp.cd(curPath);

        // todo 先给当前文件夹赋权
//        String sudoFilePath = "sudo chmod 777 " + curPath;
//        String sudoFilePath = "chmod +x " + curPath;
//        executeCmd(sudoFilePath);
        sftp.chmod(777, curPath);

        sftp.put(multipartFile.getInputStream(), fileName);

        return fileName;
    }

    /**
     * 下载文件
     * @param filePath 下载的文件路径
     * @param fileName fileName
     * @throws Exception
     */
    public InputStream downloadFile(String filePath, String fileName) throws Exception {

        if (sftp == null) {
            throw new JSchException("请先登陆远程链接!");
        }

        InputStream inputStream = sftp.get(filePath + File.separator + fileName);

        return inputStream;
    }

    /**
     * 删除文件
     * @param deleteFileName 要删除的文件
     * @param curPath 服务器当前上传的路径
     * @throws Exception
     */
    public void delete(String deleteFileName, String curPath) throws Exception {

        if (sftp == null) {
            throw new JSchException("请先登陆远程链接!");
        }

        sftp.cd(curPath);
        sftp.rm(deleteFileName);
        // disconnect(sftp);
    }

    /**
     * 列出目录下的文件
     * @param directory 要列出的目录 如: /project-api; ll
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory) throws Exception {

        if (sftp == null) {
            throw new JSchException("请先登陆远程链接!");
        }
        return sftp.ls(directory);
    }




}
