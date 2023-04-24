package com.fly.file.admin.controller;

import com.fly.common.utils.ArrayUtils;
import com.fly.common.utils.StringUtils;
import com.fly.file.admin.domain.bo.ExecuteOptionBo;
import com.fly.file.admin.utils.CommandUtils;
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
@RestController
@RequestMapping("file")
public class FileController {


    /**
     * 获取目录 (linux)
     *
     * @param path 目录地址
     */
    @PostMapping("/getList")
    public Object getList(String path, HttpServletRequest request) {

        System.out.println(request.getSession().getId());

        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, String>> fileList = new ArrayList<>();
        String cmdResult = "";

        String[] cmdList = new String[3];
        cmdList[0] = "bash";
        cmdList[1] = "-c";

        // todo 根据不同系统区分命令
        Properties prop = System.getProperties();
        String systemType = prop.getProperty("os.name");
        if (StringUtils.isNotBlank(systemType) && systemType.toLowerCase().contains("mac")) {
            cmdList[2] = "cd " + path + "; ls -lT "; // mac ( ls -l -lh )
        } else if (StringUtils.isNotBlank(systemType) && systemType.toLowerCase().contains("linux")) {
            cmdList[2] = "cd " + path + "; ls -l -lh --time-style '+%Y-%m-%d %H:%M:%S'"; // ls -l -lh --time-style '+%Y-%m-%d %H:%M:%S' -t
        } else {
            System.out.println("window");
        }

        try {

            cmdResult = CommandUtils.execLinuxCmd(cmdList);
            String[] resultList = cmdResult.split("\n");
            String[] newResultList = Arrays.copyOfRange(resultList, 1, resultList.length); // 去掉第一个的总用量

            if (newResultList.length > 0) {
                for (String data : newResultList) {

                    Map<String, String> fileMap = new HashMap<>();
                    String[] resultFileList = data.split(" ");
                    String[] newResultFileList = ArrayUtils.deleteArrayNull(resultFileList); // 去除空格元素

                    // 文件属性 文件数 拥有者 所属的group 文件大小 建档日期(yyyy-mm-dd hh:mm:ss) 文件名
                    String fileName = newResultFileList[7];
                    String fileType = newResultFileList[0].substring(0, 1);
                    fileType = fileType.equals("d") ? "1" : fileType.equals("l") ? "1" : fileType.equals("-") ? "2" : "2"; // fileType(1: 文件夹, 2:文件)
                    fileMap.put("fileType", fileType);
                    fileMap.put("permission", newResultFileList[0]);
                    fileMap.put("fileNum", newResultFileList[1]);
                    fileMap.put("userOrGroup", newResultFileList[2] + "/" + newResultFileList[3]);
                    fileMap.put("fileSize", newResultFileList[4]);
                    fileMap.put("updateTime", newResultFileList[5] + " " + newResultFileList[6]); // 把日期拼接
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

            resultMap.put("data", fileList);
            resultMap.put("resultMsg", "查询成功!");
            resultMap.put("resultCode", "1");

        } catch (Exception e) {
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
     * @param file    文件流
     * @return
     */
    @PostMapping("/uploadFile")
    public Object uploadFile(String curPath, HttpServletRequest request, @RequestParam(value = "file") MultipartFile file) {

        Map<String, Object> resultMap = new HashMap<>();
        String fileName = "";
        if (StringUtils.isBlank(curPath) || file.isEmpty()) {
            resultMap.put("resultMsg", "服务器当前路径为空 | 文件为空!");
            resultMap.put("resultCode", "1001");
            return resultMap;
        }

        try {

            fileName = file.getOriginalFilename();
            File newFile = new File(curPath + File.separator + fileName); // 上传到服务器的新文件
            FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);

            resultMap.put("data", fileName);
            resultMap.put("resultMsg", "上传成功!");
            resultMap.put("resultCode", "1");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultMsg", "系统出错!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }


    /**
     * 获取上传文件的进度
     */
    @PostMapping(value = "/uploadPercent")
    public Integer uploadStatus(HttpServletRequest request) {

        HttpSession session = request.getSession();
        Object percent = session.getAttribute("uploadPercent");

        System.out.println("=========== 当前的上传进度是: " + (Integer) percent);

        return null != percent ? (Integer) percent : 0;
    }


    /**
     * 下载文件
     *
     * @param path     下载文件路径
     * @param fileName 文件名
     */
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    @ResponseBody
    public void downloadFile(String path, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {

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
    public Object executeOption(ExecuteOptionBo executeOptionBo, HttpServletRequest request) {

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

        switch (type) {
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
                if (StringUtils.isBlank(deleteFileName)) {
                    resultMap.put("resultMsg", "要删除的文件名 -> 为空!");
                    resultMap.put("resultCode", "1001");
                    return resultMap;
                }
                cmdList[2] = "rm -rf " + curPath + File.separator + deleteFileName;
                successMes = "删除成功!";
                break;

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
                cmdList[2] = "cd " + curPath + "; chmod +x ./" + executeFileName + "; ./" + executeFileName;
                successMes = "执行成功!";
                break;
        }

        try {

            String cmdResult = CommandUtils.execLinuxCmd(cmdList);
            String[] optionData = cmdResult.split("\n");

            resultMap.put("data", optionData);
            resultMap.put("resultMsg", successMes);
            resultMap.put("resultCode", "1");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultMsg", "系统出错!");
            resultMap.put("resultCode", "1000");
        }

        return resultMap;
    }

}
