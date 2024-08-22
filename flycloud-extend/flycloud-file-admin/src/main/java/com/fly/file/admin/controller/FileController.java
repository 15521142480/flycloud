package com.fly.file.admin.controller;

import com.fly.common.model.R;
import com.fly.common.utils.ArrayUtils;
import com.fly.common.utils.StringUtils;
import com.fly.common.utils.SystemUtils;
import com.fly.file.admin.domain.bo.ExecuteOptionBo;
import com.fly.file.admin.singleton.ConFtpSingle;
import com.fly.file.admin.utils.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

/**
 * 文件模块-当前项目部署所在的服务器
 *
 * @author lxs
 * @date 2023/4/23
 */
@Slf4j
@RestController
@RequestMapping("file")
public class FileController {


    /**
     * 获取目录 (linux)
     *
     * @param path 目录地址
     */
    @PostMapping("/getList")
    public R<?> getList(@RequestBody String path, HttpServletRequest request) {

        System.out.println(request.getSession().getId());
        List<Map<String, String>> fileList = new ArrayList<>();
        String cmdResult = "";

        String[] cmdList = new String[3];
        cmdList[0] = "bash";
        cmdList[1] = "-c";

        // todo 根据不同系统区分命令；目前支持的有mac、linux
        if (SystemUtils.isMac()) {
            cmdList[2] = "cd " + path + "; ls -lT "; // mac ( ls -l -lh ); 时间貌似无法格式化
        } else if (SystemUtils.isLinux()) {
            cmdList[2] = "cd " + path + "; ls -l -lh --time-style '+%Y-%m-%d %H:%M:%S'"; // ls -l -lh --time-style '+%Y-%m-%d %H:%M:%S' -t
        } else { // 其他系统列表指令
            System.out.println("window");
        }

        try {

            cmdResult = CommandUtils.execLinuxCmd(cmdList);
            String[] resultList = cmdResult.split("\n");
            String[] newResultList = Arrays.copyOfRange(resultList, 1, resultList.length); // 去掉第一个的总用量

            if (newResultList.length > 0) {

                String fileType = "";
                String permission = "";
                String fileNum = "";
                String userOrGroup = "";
                String fileSize = "";
                String updateTime = "";
                String fileName = "";
                for (String data : newResultList) {

                    Map<String, String> fileMap = new HashMap<>();
                    String[] resultFileList = data.split(" ");
                    String[] newResultFileList = ArrayUtils.deleteArrayNull(resultFileList); // 去除空格元素

                    // mac：
                    // drwxr-xr-x@  8 lxs  staff   256 Aug 14 18:46:35 2024 applicationApp
                    // 文件属性 文件数 拥有者 所属的group 文件大小 建档日期 文件名
                    // linux：
                    // drwxr-xr-x    4 root root 4.0K 2024-06-10 11:43:04 docker
                    // 文件属性 文件数 拥有者 所属的group 文件大小 建档日期 文件名

                    if (SystemUtils.isMac()) {

                        fileType = newResultFileList[0].substring(0, 1);
                        fileType = fileType.equals("d") ? "1" : fileType.equals("l") ? "1" : fileType.equals("-") ? "2" : "2"; // fileType(1: 文件夹, 2:文件)
                        permission = newResultFileList[0];
                        fileNum = newResultFileList[1];
                        userOrGroup = newResultFileList[2] + "/" + newResultFileList[3];
                        fileSize = SystemUtils.fileSize(Long.parseLong(newResultFileList[4]));
                        updateTime = newResultFileList[8]; // 后续可把日期拼接，Aug 14 18:46:35 2024 -> yyyy-mm-dd hh:mm:ss
                        fileName = newResultFileList[9];

                    } else if (SystemUtils.isLinux()) {

                        fileType = newResultFileList[0].substring(0, 1);
                        fileType = fileType.equals("d") ? "1" : fileType.equals("l") ? "1" : fileType.equals("-") ? "2" : "2"; // fileType(1: 文件夹, 2:文件)
                        permission = newResultFileList[0];
                        fileNum = newResultFileList[1];
                        userOrGroup = newResultFileList[2] + "/" + newResultFileList[3];
                        fileSize = newResultFileList[4];
                        updateTime =  newResultFileList[5] + " " + newResultFileList[6]; // 把日期拼接成 yyyy-mm-dd hh:mm:ss;
                        fileName = newResultFileList[7];

                    } else { // 其他系统拆解

                    }

                    fileMap.put("fileType", fileType);
                    fileMap.put("permission", permission);
                    fileMap.put("fileNum", fileNum);
                    fileMap.put("userOrGroup", userOrGroup);
                    fileMap.put("fileSize", fileSize);
                    fileMap.put("updateTime", updateTime);
                    fileMap.put("fileName", fileName);


                    // 文件类型中文
                    fileMap.put("fileTypeText", fileType.equals("1") ? "文件夹" : "");
                    // 文件类型后缀
                    if (fileType.equals("2") && fileName.contains(".")) {
                        fileMap.put("type", fileName.substring(fileName.lastIndexOf(".") + 1));
                    }

                    fileList.add(fileMap);
                }
            }

        } catch (Exception e) {
            log.error("error", e);
            return R.failed("查询失败");
        }

        return R.ok(fileList, "查询成功");
    }


    /**
     * 上传文件
     *
     * @param curPath 服务器当前路径
     * @param file    文件流
     * @return
     */
    @PostMapping("/uploadFile")
    public R<?> uploadFile(String curPath, HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {

        String fileName = "";
        if (StringUtils.isBlank(curPath) || file.isEmpty()) {
            return R.failed("服务器当前路径为空 | 文件为空!");
        }

        try {
            fileName = file.getOriginalFilename();
            File newFile = new File(curPath + File.separator + fileName); // 上传到服务器的新文件
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
        } catch (Exception e) {
            log.error("error", e);
            return R.failed("上传失败");
        }

        return R.ok(fileName, "上传成功!");
    }


    /**
     * 获取上传文件的进度
     */
    @PostMapping(value = "/uploadPercent")
    public R<Integer> uploadStatus(HttpServletRequest request) {

        HttpSession session = request.getSession();
        Object percent = session.getAttribute("uploadPercent");

        log.info("=========== 当前的上传进度是: {}", percent);

        return R.ok(null != percent ? (Integer) percent : 0);
    }


    /**
     * 下载文件
     *
     * @param requestParams path     下载文件路径
     * @param requestParams fileName 文件名
     */
    @PostMapping("/downloadFile")
    public void downloadFile(@RequestBody Map<String, String> requestParams, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String path = requestParams.get("path");
        String fileName = requestParams.get("fileName");
        if (StringUtils.isBlank(path) || StringUtils.isBlank(fileName)) {
            return;
        }

        File file = new File(path + File.separator + fileName);
        if (file.exists()) {

            BufferedInputStream bis = null;
            OutputStream os = null;
            try {

                InputStream is = FileUtils.openInputStream(file);
                bis = new BufferedInputStream(is);
                os = response.getOutputStream();
                // 设置响应头信息
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream; charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "");

                byte[] buffer = new byte[500];
                int i;
                while ((i = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, i);
                }
                os.flush();

            } catch (IOException exception) {
                throw new IOException("文件下载异常");
            }

        } else {
            throw new IOException("文件不存在!!!");
        }
    }


    /**
     * 执行操作
     */
    @PostMapping("/executeOption")
    public R<?> executeOption(@RequestBody ExecuteOptionBo executeOptionBo, HttpServletRequest request) {

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
            return R.failed("服务器当前路径 | 操作类型 -> 为空!");
        }

        String successMes = "";
        String[] cmdList = new String[3];
        cmdList[0] = "bash";
        cmdList[1] = "-c";

        switch (type) {
            case "0": // 新建文件夹
                if (StringUtils.isBlank(newFolderName)) {
                    return R.failed("文件夹名称 -> 为空!");
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
                    return R.failed("旧文件/夹名 | 新文件/夹名 -> 为空!");
                }
                String oldFile = curPath + File.separator + oldFileName;
                String newFile = curPath + File.separator + newFileName;
                cmdList[2] = "mv " + oldFile + " " + newFile;
                successMes = "重命名成功!";
                break;

            case "4": // 删除文件/夹

                if (StringUtils.isBlank(deleteFileName)) {
                    return R.failed("要删除的文件名 -> 为空!");
                }
                if (!deleteFileName.contains(".")) {
                    return R.failed("只能删除文件!");
                }
//                cmdList[2] = "rm -rf " + curPath + File.separator + deleteFileName;
                cmdList[2] = "rm " + curPath + File.separator + deleteFileName;
                successMes = "删除成功!";
                break;

            case "5": // 压缩文件/夹
                if (StringUtils.isBlank(ysOrJyFileName)) {
                    return R.failed("要压缩的文件/夹 -> 为空!");
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
                    return R.failed("文件 -> 为空!");
                }
                cmdList[2] = "cd " + curPath + "; chmod +x ./" + executeFileName + "; ./" + executeFileName;
                successMes = "执行成功!";
                break;
        }

        String[] optionData = new ArrayList<>().toArray(new String[100]);
        try {
            String cmdResult = CommandUtils.execLinuxCmd(cmdList);
            optionData = cmdResult.split("\n");
        } catch (Exception e) {
            log.error("error", e);
            return R.failed("执行失败");
        }

        return R.ok(optionData, successMes);
    }


    /**
     * 执行指令
     * @param requestParams curPath 服务器当前路径
     * @param requestParams cmd 指令
     */
    @PostMapping("/executeCommand")
    public R<?> executeCommand(@RequestBody Map<String, String> requestParams, HttpServletRequest request){

        String curPath = requestParams.get("curPath");
        String cmd = requestParams.get("cmd");
        Map<String, String> dateMap = new HashMap<>();
        if (StringUtils.isBlank(curPath) || StringUtils.isBlank(cmd)) {
            return R.failed("服务器当前路径 | 指令 -> 为空!");
        }
        if (cmd.contains("rm")) {
            return R.failed("敏感指令!");
        }

        String cmdResult = "";
        try {

            String[] cmdList = new String[3];
            cmdList[0] = "bash";
            cmdList[1] = "-c";

            String cmdStr = "cd " + curPath + "; " + cmd;

            if (SystemUtils.isMac()) {
                cmdResult = CommandUtils.execMacCmd(cmdStr);
            } else if (SystemUtils.isLinux()) {
                cmdList[2] = cmdStr;
                cmdResult = CommandUtils.execLinuxCmd(cmdList); // linux 需加上bash -c
            } else {
//                cmdResult = XXXX;
            }

            // todo cd命令处理最新目录路径
            String new_cur_path = "";
            if (cmd.contains("cd")) {

//                new_cur_path = "/" + cmdStr.substring(cmdStr.lastIndexOf("cd") + 3);
//                if (new_cur_path.contains("~")) {
//                    new_cur_path = "/";
//                }
//                if (new_cur_path.contains("../")) {
//                    new_cur_path = curPath.substring(0, curPath.lastIndexOf("/"));
//                }
//                if (new_cur_path.contains("./")) {
//                    new_cur_path = curPath + "/" + cmdStr.substring(cmdStr.lastIndexOf("./") + 2);
//                }
//
//                if (StringUtils.isBlank(new_cur_path)) {
//                    new_cur_path = "/";
//                }

                if (SystemUtils.isMac()) {
                    String new_cur_path2 = CommandUtils.execMacCmd(cmdStr + "; " + "pwd");
                    new_cur_path = new_cur_path2.replaceAll("\n", "");
                } else if (SystemUtils.isLinux()) {

                    cmdList[2] = cmdStr + "; " + "pwd";
                    String new_cur_path2 = CommandUtils.execLinuxCmd(cmdList);
                    new_cur_path = new_cur_path2.replaceAll("\n", "");
                } else {

                }
            }

            dateMap.put("cmdResult", cmdResult);
            dateMap.put("newCurPath", new_cur_path);

        }catch (Exception e){
            log.error("error", e);
            return R.failed("系统出错!");
        }

        return R.ok(dateMap, "执行成功");
    }

}
