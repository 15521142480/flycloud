package com.fly.file.admin.singleton;

/**
 * 上传单例
 *
 * @author lxs
 * @date 2023/4/24
 */
public class UploadFileScheduleSingle {

    private static final UploadFileScheduleSingle INSTEANS = new UploadFileScheduleSingle();

    private long transfered; // 记录已传输的数据总大小

    private long fileSize; // 记录文件总大小
    //进度
    private String plan ="0.00";

    public long getTransfered() {
        return transfered;
    }

    public void setTransfered(long transfered) {
        this.transfered = transfered;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    private UploadFileScheduleSingle(){

    }
    public static UploadFileScheduleSingle getInsteans(){
        return INSTEANS;
    }
}
