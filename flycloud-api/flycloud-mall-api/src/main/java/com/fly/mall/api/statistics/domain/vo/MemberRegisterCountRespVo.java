package com.fly.mall.api.statistics.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 会员注册数量响应对象。
 *
 * @author lxs
 * @date 2026-07-01
 */
@Data
public class MemberRegisterCountRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Integer count;

}
