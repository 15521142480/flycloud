package com.fly.test.domain.vo;

import com.fly.bpm.api.domain.vo.SeataTestDataVo;
import lombok.Data;

import java.util.List;

/**
 * Seata 两个数据源的测试数据集合。
 *
 * @author lxs
 */
@Data
public class SeataTestDataListVo {

    /**
     * 数据源一（fly_cloud）的最新测试数据。
     */
    private List<SeataTestDataVo> dataSourceOne;

    /**
     * 数据源二（fly_seata）的最新测试数据。
     */
    private List<SeataTestDataVo> dataSourceTwo;

}
