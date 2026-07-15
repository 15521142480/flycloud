package com.fly.system.api.member.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fly.common.domain.BaseEntity;
import com.fly.system.api.im.typehandler.LongListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员用户。
 *
 * @author lxs
 * @date 2026-07-02
 */
@TableName("member_user")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "会员用户对象", description = "会员用户")
public class MemberUser extends BaseEntity {

    // ========== 账号信息 ==========

    /**
     * 用户ID
     */
    @TableId
    private Long id;

    /**
     * 手机
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 加密后的密码
     *
     * 因为目前使用 {@link BCryptPasswordEncoder} 加密器，所以无需自己处理 salt 盐
     */
    private String password;

    /**
     * 帐号状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;

    /**
     * 注册 IP
     */
    private String registerIp;

    /**
     * 注册终端
     * 枚举 {@link TerminalEnum}
     */
    private Integer registerTerminal;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;


    // ========== 基础信息 ==========

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 真实名字
     */
    private String name;

    /**
     * 性别
     *
     * 枚举 {@link SexEnum}
     */
    private Integer sex;

    /**
     * 出生日期
     */
    private LocalDateTime birthday;

    /**
     * 所在地
     *
     * 关联 {@link Area#getId()} 字段
     */
    private Integer areaId;

    /**
     * 用户备注
     */
    private String mark;

    // ========== 其它信息 ==========

    /**
     * 积分
     */
    private Integer point;

    /**
     * 会员标签列表，以逗号分隔
     */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> tagIds;

    /**
     * 岗位编号数组，以逗号分隔存储。
     */
    @TableField(typeHandler = LongListTypeHandler.class)
    private List<Long> postIds;

    /**
     * 会员级别编号
     *
     * 关联 {@link MemberLevelDO#getId()} 字段
     */
    private Long levelId;

    /**
     * 会员经验
     */
    private Integer experience;

    /**
     * 用户分组编号
     *
     * 关联 {@link MemberGroupDO#getId()} 字段
     */
    private Long groupId;


}
