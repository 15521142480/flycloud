package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.SeckillConfigBo;
import com.fly.mall.api.promotion.domain.vo.AppSeckillConfigRespVo;
import com.fly.mall.api.promotion.domain.vo.SeckillConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 秒杀时段配置 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ISeckillConfigService {

    /**
     * 查询秒杀时段配置详情。
     */
    SeckillConfigVo queryById(Long id);

    /**
     * 分页查询秒杀时段配置。
     */
    PageVo<SeckillConfigVo> queryPageList(SeckillConfigBo bo, PageBo pageBo);

    /**
     * 查询秒杀时段配置列表。
     */
    List<SeckillConfigVo> queryList(SeckillConfigBo bo);

    /**
     * 查询启用的移动端秒杀时间段列表。
     */
    List<AppSeckillConfigRespVo> queryAppList();

    /**
     * 查询当前正在进行的秒杀时间段。
     */
    SeckillConfigVo queryCurrentConfig();

    /**
     * 新增或修改秒杀时段配置。
     */
    Boolean saveOrUpdate(SeckillConfigBo bo);

    /**
     * 校验并批量删除秒杀时段配置。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
