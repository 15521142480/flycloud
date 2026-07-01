package com.fly.file.controller.app;

import cn.hutool.core.io.IoUtil;
import com.fly.common.domain.model.R;
import com.fly.file.service.FileService;
import com.fly.system.api.file.domain.vo.FileUploadReqVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * app文件-控制器。
 *
 * @author lxs
 * @date 2026-07-01
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/file")
public class AppFileController {


    private final FileService fileService;


    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件")
    @Parameter(name = "file", description = "文件附件", required = true,
            schema = @Schema(type = "string", format = "binary"))
    public R<String> uploadFile(@Valid FileUploadReqVo uploadReqVO) throws Exception {

        MultipartFile file = uploadReqVO.getFile();
        byte[] content = IoUtil.readBytes(file.getInputStream());
        // app 多一个app文件夹区分
        String filePath = fileService.uploadFile(
                content,
                file.getOriginalFilename(),
                File.separator + "app" + File.separator + uploadReqVO.getDirectory(),
                file.getContentType()
        );
        return R.ok(filePath);
    }

}
