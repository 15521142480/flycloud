package com.fly.mall.api.promotion.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 移动端砍价记录概要返回对象。
 *
 * @author lxs
 * @date 2026-07-03
 */
@Data
public class AppBargainRecordSummaryRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer successUserCount;

    private List<Record> successList;

    /**
     * 成功砍价记录摘要。
     */
    @Data
    public static class Record implements Serializable {

        private static final long serialVersionUID = 1L;

        private String nickname;

        private String avatar;

        private String activityName;

    }

}
