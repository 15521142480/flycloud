package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.SeckillProductBo;
import com.fly.mall.api.promotion.domain.vo.SeckillProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 秒杀商品 Service 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ISeckillProductService {

    /**
     * 查询秒杀商品详情。
     */
    SeckillProductVo queryById(Long id);

    /**
     * 分页查询秒杀商品。
     */
    PageVo<SeckillProductVo> queryPageList(SeckillProductBo bo, PageBo pageBo);

    /**
     * 查询秒杀商品列表。
     */
    List<SeckillProductVo> queryList(SeckillProductBo bo);

    /**
     * 新增或修改秒杀商品。
     */
    Boolean saveOrUpdate(SeckillProductBo bo);

    /**
     * 校验并批量删除秒杀商品。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
