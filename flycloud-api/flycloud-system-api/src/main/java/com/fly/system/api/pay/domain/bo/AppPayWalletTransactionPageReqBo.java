package com.fly.system.api.pay.domain.bo;

import com.fly.common.domain.bo.PageBo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 移动端 - 钱包流水分页请求对象。
 *
 * @author lxs
 * @date 2026-06-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "移动端 - 钱包流水分页请求对象")
public class AppPayWalletTransactionPageReqBo extends PageBo {

    public static final Integer TYPE_INCOME = 1;

    public static final Integer TYPE_EXPENSE = 2;

    @Schema(description = "类型：1 收入，2 支出")
    private Integer type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间范围")
    private LocalDateTime[] createTime;

}
