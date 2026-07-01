package com.fly.file.factory;

import com.fly.common.config.properties.FileConfigProperties;
import com.fly.common.enums.FileUseServerTypeEnum;
import com.fly.file.factory.service.FileClientService;
import com.fly.file.factory.service.impl.LocalFileClientServiceImpl;
import com.fly.file.factory.service.impl.SftpFileClientServiceImpl;
import com.fly.common.utils.spring.SpringUtils;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 文件-工厂
 *
 * @author lxs
 * @date 2026/07/01
 */
public class FileClientFactory {


    // 定义枚举类型获取接口
    private static final Map<FileUseServerTypeEnum, Supplier<FileClientService>> factoryMap = new EnumMap<>(FileUseServerTypeEnum.class);;


    // 当前使用的资源类型
    public static int curUseServerType = 3;


    // 根据枚举类型获取接口（类加载就加载bean可能spring容器没准备好）
//    static {
//        factoryMap.put(FileUseServerTypeEnum.LOCAL, SpringUtils.getBean("localFileServiceImpl", LocalFileServiceImpl.class));
//        factoryMap.put(FileUseServerTypeEnum.SFTP, SpringUtils.getBean("sftpFileServiceImpl", SftpFileServiceImpl.class));
//    }

    // 优化，使用才构建bean，Spring 容器基本已经准备好了
    static {
        factoryMap.put(FileUseServerTypeEnum.LOCAL,
                () -> SpringUtils.getBean(LocalFileClientServiceImpl.class));

        factoryMap.put(FileUseServerTypeEnum.SFTP,
                () -> SpringUtils.getBean(SftpFileClientServiceImpl.class));
    }



    /**
     * 执行文件上传
     *
    */
    public static FileClientService getFileService() {

        FileConfigProperties uploadConfigProperties = SpringUtils.getBean(FileConfigProperties.class);
        int useServerType = uploadConfigProperties.getUseServerType();
        curUseServerType = useServerType;
        FileUseServerTypeEnum typeEnum = FileUseServerTypeEnum.getEnum(useServerType);
        if (typeEnum == null) {
            throw new IllegalArgumentException("不支持的文件服务类型：" + useServerType);
        }
        Supplier<FileClientService> supplier = factoryMap.get(typeEnum);
        if (supplier == null) {
            throw new IllegalArgumentException("未配置文件服务实现：" + typeEnum);
        }

        return supplier.get();
    }


    public static FileClientService getFileServiceByType(int useServerType) {

        curUseServerType = useServerType;
        FileUseServerTypeEnum typeEnum = FileUseServerTypeEnum.getEnum(useServerType);
        if (typeEnum == null) {
            throw new IllegalArgumentException("不支持的文件服务类型：" + useServerType);
        }
        Supplier<FileClientService> supplier = factoryMap.get(typeEnum);
        if (supplier == null) {
            throw new IllegalArgumentException("未配置文件服务实现：" + typeEnum);
        }

        return supplier.get();
    }

}
