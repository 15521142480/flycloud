package com.fly.file.admin.controller;

import com.fly.common.utils.ArrayUtils;
import com.fly.common.utils.Base64Utils;
import com.fly.common.utils.StringUtils;
import com.fly.file.admin.domain.bo.ConnectFtpBo;
import com.fly.file.admin.domain.bo.ExecuteOptionBo;
import com.fly.file.admin.singleton.ConFtpSingle;
import com.fly.file.admin.singleton.UploadFileScheduleSingle;
import com.jcraft.jsch.JSchException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * 文件ftp模块-远程
 *
 * @author lxs
 * @date 2023/4/24
 */
@RestController
@RequestMapping("/fileFtp")
public class FileFtpController {


    UploadFileScheduleSingle uploadFileScheduleSingle = UploadFileScheduleSingle.getInsteans();

    
    
    /**
     * 登陆ftp
     * 
     * @param user 用户
     * @param port 端口
     * @param baseKey ip与密码base64
    */
    @PostMapping("/login")
    public Object login(String user, String port, String baseKey, HttpServletRequest request){

        Map<String, Object> resultMap = new HashMap<>();

        if (StringUtils.isBlank(baseKey) || StringUtils.isBlank(user) || StringUtils.isBlank(port)) {
            resultMap.put("resultMsg", "baseKey|用户|端口都不能为空!");
            resultMap.put("resultCode", "1001");
            return resultMap;
        }

        try {

            String newBase64 = baseKey.substring(4) + baseKey.substring(0, 4);
            String newBase = Base64Utils.decode64(newBase64); // ip#密码
            String host = newBase.split("@@@")[0];
            String password = newBase.split("@@@")[1];

            ConnectFtpBo conBean = new ConnectFtpBo();
            conBean.setHost(host);
            conBean.setUser(user);
            conBean.setPassword(password);
            conBean.setPort(Integer.parseInt(port));

            ConFtpSingle singleton = ConFtpSingle.getConFtpSingle(); // 生成单例
            singleton.loginConnect(conBean); // 登陆ftp
            resultMap.put("resultMsg", "ftp连接成功!");
            resultMap.put("resultCode", "1");

        }catch (JSchException e) {
            e.printStackTrace();
            resultMap.put("resultMsg", "ftp连接失败!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 登出
     *
     */
    @PostMapping("/loginOut")
    public Object loginOut(HttpServletRequest request){

        Map<String, Object> resultMap = new HashMap<>();

        try {
            ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
            singleton.loginOutConnect();
            resultMap.put("resultMsg", "ftp登出成功!");
            resultMap.put("resultCode", "1");

        }catch (JSchException e) {
            e.printStackTrace();
            resultMap.put("resultMsg", "ftp登出失败!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 是否在连接
     *
     */
    @PostMapping("/isConnect")
    public Object isConnect(){

        Map<String, Object> resultMap = new HashMap<>();
        try {

            ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
            resultMap = singleton.getConnect();

        }catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultMsg", "验证失败!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 获取列表
     *
     * @param path 路径
     */
    @PostMapping("/getList")
    public Object getList(String path, HttpServletRequest request){

        Map<String, Object> resultMap = new HashMap<>();
        ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
        String cmdResult = "";

        try {

            // 1.使用内置列表方法显示文件列表
//            Vector vectorList = ftpUtils.listFiles("/project-api");
//            List<Map<String, Object>> directoryList = new ArrayList<>();
//
//            if (vectorList.size() > 0) {
//                List<ChannelSftp.LsEntry> list = Collections.list(vectorList.elements());
//                for (ChannelSftp.LsEntry data : list) {
//                    Map<String, Object> fileData = new HashMap<>();
//                    fileData.put("name", data.getFilename());
//                    String longInfo = data.getLongname(); // 文件属性 文件数 拥有者 所属的group 文件大小 建档日期 文件名
//                    String[] longList = longInfo.split(" ");
//                    directoryList.add(fileData);
//                }ftpUtils.listFiles("/project-api");
//            }
//            System.out.println(directoryList);

            // 2.使用命令方法显示文件列表(自定义信息)
            List<Map<String, String>> fileList = new ArrayList<>();
            cmdResult = singleton.executeCmd("cd " + path + "; ls -l -lh --time-style '+%Y-%m-%d %H:%M:%S' -t");
            // cmdResult = singleton.executeCmd("cd /project-api; ls -l -lh --time-style '+%Y-%m-%d %H:%M:%S' -t");
            String[] resultList = cmdResult.split("\n");
            String[] newResultList = Arrays.copyOfRange(resultList, 1, resultList.length); // 去掉第一个的总用量

            if (newResultList.length > 0) {
                for (String data : newResultList) {

                    Map<String, String> fileMap = new HashMap<>();
                    String[] resultFileList = data.split(" ");
                    String[] newResultFileList = ArrayUtils.deleteArrayNull(resultFileList); // 去除空格元素

                    // 文件属性 文件数 拥有者 所属的group 文件大小 建档日期(yyyy-mm-dd hh:mm:ss) 文件名
                    String fileType = newResultFileList[0].substring(0, 1);
                    fileType = fileType.equals("d") ? "1" : fileType.equals("l") ? "1" : fileType.equals("-") ? "2" : "2"; // fileType(1: 文件夹, 2:文件)
                    fileMap.put("fileType", fileType);
                    fileMap.put("fileTypeText", fileType.equals("1") ? "文件夹" : "");
                    fileMap.put("permission", newResultFileList[0]);
                    fileMap.put("fileNum", newResultFileList[1]);
                    fileMap.put("userOrGroup", newResultFileList[2] + "/" + newResultFileList[3]);
                    fileMap.put("fileSize", newResultFileList[4]);
                    fileMap.put("updateTime", newResultFileList[5] + " " + newResultFileList[6]); // 把日期拼接
                    fileMap.put("fileName", newResultFileList[7]);
                    fileList.add(fileMap);
                }
            }

            resultMap.put("data", fileList);
            resultMap.put("resultMsg", "查询成功!");
            resultMap.put("resultCode", "1");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("resultMsg", "系统出错!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 上传文件
     *
     * @param curPath 服务器当前路径
     */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Object uploadFile(String curPath, HttpServletRequest request, @RequestParam(value = "file") MultipartFile file){

        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isBlank(curPath) || file.isEmpty()) {
            resultMap.put("resultMsg", "服务器当前路径为空 | 文件为空!");
            resultMap.put("resultCode", "1001");
            return resultMap;
        }

        try {

            ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
            String fileName = singleton.uploadFile(file, curPath);

            resultMap.put("data", fileName);
            resultMap.put("resultMsg", "上传成功!");
            resultMap.put("resultCode", "1");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("resultMsg", "系统出错!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 获取上传进度
     *
     */
    @GetMapping("/getFileUploadProgress")
    public UploadFileScheduleSingle getFileUploadProgress(){
        return uploadFileScheduleSingle;
    }


    /**
     * 上传文件
     *
     * @param path 下载文件路径
     * @param fileName 文件名
     */
    @GetMapping("/downloadFile")
    public Object downloadFile(String path, String fileName, HttpServletRequest request, HttpServletResponse response){

        ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
        BufferedInputStream bis = null;
        OutputStream os = null;

        try {

            InputStream is = singleton.downloadFile(path, fileName);
            bis = new BufferedInputStream(is);
            os = response.getOutputStream();
            // 设置响应头信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName+"");

            byte[] buffer = new byte[500];
            int i;
            while ((i = bis.read(buffer)) != -1) {
                os.write(buffer, 0, i);
            }
            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 执行操作
     *
     */
    @PostMapping("/executeOption")
    public Object executeOption(ExecuteOptionBo executeOptionBo, HttpServletRequest request){

        Map<String, Object> resultMap = new HashMap<>();
        String curPath = executeOptionBo.getCurPath();
        String type = executeOptionBo.getType();
        String executeFileName = executeOptionBo.getExecuteFileName();
        String newFolderName = executeOptionBo.getNewFolderName();
        String oldFileName = executeOptionBo.getOldFileName();
        String newFileName = executeOptionBo.getNewFileName();
        String deleteFileName = executeOptionBo.getDeleteFileName();
        String ysOrJyFileName = executeOptionBo.getYsOrJyFileName();
        String ysToFileName = executeOptionBo.getYsToFileName();

        if (StringUtils.isBlank(type) || StringUtils.isBlank(curPath)) {
            resultMap.put("resultMsg", "服务器当前路径 | 操作类型 -> 为空!");
            resultMap.put("resultCode", "1001");
            return resultMap;
        }

        String successMes = "";
        String[] cmdList = new String[3];
        cmdList[0] = "bash";
        cmdList[1] = "-c";

        switch (type){
            case "0": // 新建文件夹
                if (StringUtils.isBlank(newFolderName)) {
                    resultMap.put("resultMsg", "文件夹名称 -> 为空!");
                    resultMap.put("resultCode", "1001");
                    return resultMap;
                }
                cmdList[2] = "mkdir " + curPath + File.separator + newFolderName;
                successMes = "新建文件夹成功!";
                break;

            case "1": // 启动服务
                cmdList[2] = "cd " + curPath + "; chmod +x ./start.sh; ./start.sh";
                successMes = "启动服务成功!";
                break;

            case "2": // 关闭服务
                cmdList[2] = "cd " + curPath + "; chmod +x ./stop.sh; ./stop.sh";
                successMes = "关闭服务成功!";
                break;

            case "3": // 重命名
                if (StringUtils.isBlank(oldFileName) || StringUtils.isBlank(newFileName)) {
                    resultMap.put("resultMsg", "旧文件/夹名 | 新文件/夹名 -> 为空!");
                    resultMap.put("resultCode", "1001");
                    return resultMap;
                }
                String oldFile = curPath + File.separator + oldFileName;
                String newFile = curPath + File.separator + newFileName;
                cmdList[2] = "mv " + oldFile + " " + newFile;
                successMes = "重命名成功!";
                break;

            case "4": // 删除文件/夹
                resultMap.put("resultMsg", "危险指令，无法操作!");
                resultMap.put("resultCode", "1001");
                return resultMap;

//                if (StringUtils.isBlank(deleteFileName)) {
//                    resultMap.put("resultMsg", "要删除的文件名 -> 为空!");
//                    resultMap.put("resultCode", "1001");
//                    return resultMap;
//                }
//                cmdList[2] = "rm -rf " + curPath + File.separator + deleteFileName;
//                successMes = "删除成功!";
//                break;

            case "5": // 压缩文件/夹
                if (StringUtils.isBlank(ysOrJyFileName)) {
                    resultMap.put("resultMsg", "要压缩的文件/夹 -> 为空!");
                    resultMap.put("resultCode", "1001");
                    return resultMap;
                }
                if (StringUtils.isBlank(ysToFileName)) {
                    ysToFileName = ysOrJyFileName;
                }
                cmdList[2] = "cd " + curPath + " ; tar -cvf " + ysToFileName + ".tar" + " " + ysOrJyFileName;
                successMes = "压缩成功!";
                break;

//            case "6": // 解压文件
//                if (StringUtils.isBlank(ysOrJyFileName)) {
//                    resultMap.put("resultMsg", "压缩文件 -> 为空!");
//                    resultMap.put("resultCode", "1001");
//                    return resultMap;
//                }
//                cmdList[2] = "cd " + curPath + " ; chmod +x ./stop.sh; ./stop.sh";
//                successMes = "删除成功!";
//                break;

            case "10": // 执行文件
                if (StringUtils.isBlank(executeFileName)) {
                    resultMap.put("resultMsg", "文件 -> 为空!");
                    resultMap.put("resultCode", "1001");
                    return resultMap;
                }
                cmdList[2] = "cd " + curPath + "; chmod +x ./"+executeFileName+"; ./"+executeFileName;
                successMes = "执行成功!";
                break;
        }

        try {

            ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
            String cmdResult = singleton.executeCmd(cmdList[2]); // todo 执行最后一条
            String[] optionData = cmdResult.split("\n");

            resultMap.put("data", optionData);
            resultMap.put("resultMsg", successMes);
            resultMap.put("resultCode", "1");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("resultMsg", "系统出错!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 执行指令
     * @param curPath 服务器当前路径
     * @param cmd 指令
     */
    @PostMapping("/executeCommand")
    public Object executeCommand(String curPath, String cmd, HttpServletRequest request){

        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isBlank(curPath) || StringUtils.isBlank(cmd)) {
            resultMap.put("resultMsg", "服务器当前路径 | 指令 -> 为空!");
            resultMap.put("resultCode", "1001");
            return resultMap;
        }

        if (cmd.contains("rm")) {
            resultMap.put("resultMsg", "敏感指令!");
            resultMap.put("resultCode", "1002");
            return resultMap;
        }

        try {

            String cmdStr = "cd " + curPath + "; " + cmd;
            ConFtpSingle singleton = ConFtpSingle.getConFtpSingle();
            String cmdResult = singleton.executeCmd(cmdStr);

            // todo cd命令处理
            String new_cur_path = "";
            if (cmd.contains("cd")) {
                String new_cur_path2 = singleton.executeCmd("pwd");
//                new_cur_path = new_cur_path2.replaceAll("\n", "");
                new_cur_path = "/" + cmdStr.substring(cmdStr.lastIndexOf("cd") + 3);
                if (new_cur_path.contains("~")) {
                    new_cur_path = "/";
                }
                if (new_cur_path.contains("../")) {
                    new_cur_path = curPath.substring(0, curPath.lastIndexOf("/"));
                }
                if (new_cur_path.contains("./")) {
                    new_cur_path = curPath + "/" + cmdStr.substring(cmdStr.lastIndexOf("./") + 2);
                }

                if (StringUtils.isBlank(new_cur_path)) {
                    new_cur_path = "/";
                }
            }

            resultMap.put("data", cmdResult);
            resultMap.put("newCurPath", new_cur_path);
            resultMap.put("resultMsg", "执行成功");
            resultMap.put("resultCode", "1");

        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("resultMsg", "系统出错!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


}
