package com.fly.system.api.member.domain.bo;

import com.fly.common.domain.BaseEntity;
import com.fly.common.utils.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员用户业务对象。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "会员用户业务对象")
public class MemberUserBo extends BaseEntity {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23788")
//    @NotNull(message = "编号不能为空")
    private Long id;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15601691300")
    @NotNull(message = "手机号不能为空")
    private String mobile;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "邮箱", example = "member@iocoder.cn")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotNull(message = "用户昵称不能为空")
    private String nickname;

    @Schema(description = "头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "/xx/xx/x.png")
//    @URL(message = "头像必须是 URL 格式")
    private String avatar;

    @Schema(description = "用户昵称", example = "李四")
    private String name;

    @Schema(description = "用户性别", example = "1")
    private Integer sex;

    @Schema(description = "所在地编号", example = "4371")
    private Long areaId;

    @Schema(description = "所在地全程", example = "上海上海市普陀区")
    private String areaName;

    @Schema(description = "出生日期", example = "2023-03-12")
    @DateTimeFormat(pattern = DateUtils.YYYY_MM_DD)
    private LocalDateTime birthday;

    @Schema(description = "会员备注", example = "我是小备注")
    private String mark;

    @Schema(description = "会员标签", example = "[1, 2]")
    private List<Long> tagIds;

    @Schema(description = "会员等级编号", example = "1")
    private Long levelId;

    @Schema(description = "用户分组编号", example = "1")
    private Long groupId;

}
