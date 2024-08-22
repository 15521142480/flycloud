package com.fly.file.admin.utils;

import com.fly.common.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
//            log.info("\033[0;35m" + line + "\033[0m");
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
     *执行linux命令
     * @param cmdList
     * @throws IOException
     * @throws InterruptedException
     */
    public static String execLinuxCmd(String[] cmdList) throws IOException, InterruptedException {

        log.info("Linux执行指令：{}", Arrays.toString(cmdList));
        Process ps = Runtime.getRuntime().exec(cmdList);
        ps.waitFor();

        String result = IOUtils.toString(ps.getInputStream(), "UTF-8");
        log.info("Linux执行指令成功，返回：{}", result);

        return result;
    }


    /**
     *执行linux命令
     * @param cmdList
     * @throws IOException
     * @throws InterruptedException
     */
    public static String execLinuxCmd(String cmdList) throws IOException, InterruptedException {

        log.info("Linux执行指令：{}", cmdList);
        Process ps = Runtime.getRuntime().exec(cmdList);
        ps.waitFor();

        String result = IOUtils.toString(ps.getInputStream(), "UTF-8");
        log.info("Linux执行指令成功，返回：{}", result);

        return result;
    }


    /**
     *执行mac命令
     *
     * @param command // String[] cmdList,
    */
    public static String execMacCmd(String command) {

        log.info("Mac执行指令：{}", command);
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);

        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuffer output = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                log.info("Mac执行指令成功，返回：{}", output.toString());
                return output.toString();
            } else {
                // handle non-zero exit code if needed
                return null;
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }



    // ================================================================================= 测试

    public static void main(String[] args) {


        // todo mac 测试
        String output = execMacCmd("cd /git; ls");
//        log.info(output);


        // todo linux 测试
//        //单行字符串命令
//        //字符串列表命令
//        List<String> cmdList = new ArrayList<>();
//          //cmdList.add("pwd");
//        cmdList.add("cmd");
//        cmdList.add("/c");
//        cmdList.add("cd /Users/laixueshi/");
//
//        try {
//
////            Result r = CommandUtils.run(new File("/Users/laixueshi/"), cmdList);
////            log.info("code:" + r.code + "\n数据:" + r.data);
//
//            // String[] cmdList2 = {"cd /Users/laixueshi/", "ls"};
//            String[] cmdList2 = {"ls"};
//            execLinuxCmd(cmdList2);
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }


    }


}
