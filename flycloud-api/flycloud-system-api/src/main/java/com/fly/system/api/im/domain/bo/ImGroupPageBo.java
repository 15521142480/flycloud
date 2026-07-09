package com.fly.system.api.im.domain.bo;

import lombok.*;
import lombok.experimental.Accessors;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fly.common.domain.bo.PageBo;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.fly.system.api.im.constants.ImDateConstants.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 群分页 Request VO")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImGroupPageBo extends PageBo {

    @Schema(description = "群名称", example = "张三")
    private String name;

    @Schema(description = "群主用户编号", example = "31460")
    private Long ownerUserId;

    @Schema(description = "群公告")
    private String notice;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
