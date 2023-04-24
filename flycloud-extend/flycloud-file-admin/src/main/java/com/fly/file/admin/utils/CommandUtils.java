package com.fly.file.admin.utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 命令处理
 *
 * @author lxs
 * @date 2023/4/23
 */
public class CommandUtils {

    /**
     * 默认字符集
     */
    private final static String DEFAULT_CHARSET_NAME = "UTF-8";

    /**
     * 创建进程并执行指令返回结果
     *
     * @param commend 子进程执行的命令
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public static Result run(File bsdir,String commend) throws IOException, InterruptedException {

        return run(bsdir,commend, DEFAULT_CHARSET_NAME);
    }

    /**
     * 创建进程并执行指令返回结果
     *
     * @param commend     子进程执行的命令
     * @param charsetName 字符集
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public static Result run(File bsdir,String commend, String charsetName) throws IOException, InterruptedException {
        StringTokenizer st = new StringTokenizer(commend);
        String[] commendArray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            commendArray[i] = st.nextToken();
        }
        return run(bsdir,Arrays.asList(commendArray), charsetName);
    }

    /**
     * 创建进程并执行指令返回结果
     *
     * @param cmdList 子进程执行的命令
     * @return
     * @throws InterruptedException
     * @throws IOException
     */
    public static Result run(File filePath, List<String> cmdList) throws IOException, InterruptedException {
        return run(filePath, cmdList, DEFAULT_CHARSET_NAME);
    }

    /**
     * 创建进程并执行指令返回结果
     *
     * @param cmdList     子进程执行的命令
     * @param charsetName 字符集
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static Result run(File path, List<String> cmdList, String charsetName) throws IOException, InterruptedException {

        Result result = new Result();
        InputStream is = null;

        // 重定向异常输出流
        ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
        // processBuilder.directory(path);
        processBuilder.redirectErrorStream(true); // 将错误输出流转移到标准输出流中,但使用Runtime不可以
        Process process = processBuilder.start();

        // 读取输入流中的数据
        is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charsetName));
        StringBuilder data = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            data.append(line).append(System.lineSeparator());
            System.out.println("\033[0;35m" + line + "\033[0m");
        }
        // 获取返回码
        result.code = process.waitFor();
        // 获取执行结果
        result.data = data.toString().trim();
        closeStreamQuietly(is);

        return result;
    }

    /**
     * 关闭数据流
     *
     * @param stream
     */
    private static void closeStreamQuietly(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进程处理结果实体类
     */
    public static class Result {
        /**
         * 返回码，0：正常，其他：异常
         */
        public int code;
        /**
         * 返回结果
         */
        public String data;
    }

    /**
     * 执行linux命令
     * @param cmdList
     * @throws IOException
     * @throws InterruptedException
     */
    public static String execLinuxCmd(String[] cmdList) throws IOException, InterruptedException {

        Process ps = Runtime.getRuntime().exec(cmdList);
        ps.waitFor();

        String result = IOUtils.toString(ps.getInputStream(), "UTF-8");
        System.out.println("Linux 命令执行成功");
        System.out.println(result);

        return result;
    }


    /**
     * 执行linux命令
     * @param cmdList
     * @throws IOException
     * @throws InterruptedException
     */
    public static String execLinuxCmd(String cmdList) throws IOException, InterruptedException {

        Process ps = Runtime.getRuntime().exec(cmdList);
        ps.waitFor();

        String result = IOUtils.toString(ps.getInputStream(), "UTF-8");
        System.out.println("Linux 命令执行成功");
        System.out.println(result);

        return result;
    }

    // ============================= 测试
    public static void main(String[] args) {

        //单行字符串命令
//		Result r = ProcessUtils.run("cmd /C ipconfig /all", "GBK");
//		System.out.println("code:" + r.code + "\ndata:" + r.data);
        //字符串列表命令
        List<String> cmdList = new ArrayList<>();
          //cmdList.add("pwd");
        cmdList.add("cmd");
        cmdList.add("/c");
        cmdList.add("cd /Users/laixueshi/");

        try {

//            Result r = CommandUtils.run(new File("/Users/laixueshi/"), cmdList);
//            System.out.println("code:" + r.code + "\n数据:" + r.data);

            // String[] cmdList2 = {"cd /Users/laixueshi/", "ls"};
            String[] cmdList2 = {"ls"};
            execLinuxCmd(cmdList2);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
