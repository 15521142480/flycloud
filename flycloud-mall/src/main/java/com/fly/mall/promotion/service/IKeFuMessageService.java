package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.KeFuMessageBo;
import com.fly.mall.api.domain.promotion.vo.KeFuMessageVo;

import java.util.Collection;
import java.util.List;

/**
 * 客服消息 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IKeFuMessageService {

    /**
     * 查询客服消息详情。
     */
    KeFuMessageVo queryById(Long id);

    /**
     * 分页查询客服消息。
     */
    PageVo<KeFuMessageVo> queryPageList(KeFuMessageBo bo, PageBo pageBo);

    /**
     * 查询客服消息列表。
     */
    List<KeFuMessageVo> queryList(KeFuMessageBo bo);

    /**
     * 新增或修改客服消息。
     */
    Boolean saveOrUpdate(KeFuMessageBo bo);

    /**
     * 校验并批量删除客服消息。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
