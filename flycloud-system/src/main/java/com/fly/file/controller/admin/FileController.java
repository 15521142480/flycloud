package com.fly.file.controller.admin;

import cn.hutool.core.io.IoUtil;
import com.fly.common.domain.model.R;
import com.fly.file.service.FileService;
import com.fly.file.service.FileUrlService;
import com.fly.system.api.file.domain.vo.FileConfigRespVo;
import com.fly.system.api.file.domain.vo.FileUploadReqVo;
import com.fly.system.api.file.domain.vo.FileUploadRespVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件-控制器。
 *
 * @author lxs
 * @date 2026-07-01
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/file")
public class FileController {


    private final FileService fileService;
    private final FileUrlService fileUrlService;


    /**
     * 获取当前文件存储配置。
     */
    @GetMapping("/config")
    @Operation(summary = "获取文件配置", description = "获取当前文件存储基础路径和访问地址")
    public R<FileConfigRespVo> getConfig() {
        return R.ok(new FileConfigRespVo(fileUrlService.getBasePath(), fileUrlService.getBaseUrl()));
    }


    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件")
    @Parameter(name = "file", description = "文件附件", required = true,
            schema = @Schema(type = "string", format = "binary"))
    public R<FileUploadRespVo> uploadFile(@Valid FileUploadReqVo uploadReqVO) throws Exception {

        MultipartFile file = uploadReqVO.getFile();
        byte[] content = IoUtil.readBytes(file.getInputStream());
        FileUploadRespVo fileUploadRespVo = fileService.uploadFile(
                content,
                file.getOriginalFilename(),
                uploadReqVO.getDirectory(),
                file.getContentType()
        );
        return R.ok(fileUploadRespVo);
    }

}
