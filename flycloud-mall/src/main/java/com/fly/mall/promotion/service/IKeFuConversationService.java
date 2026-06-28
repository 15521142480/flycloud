package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.promotion.bo.KeFuConversationBo;
import com.fly.mall.api.domain.promotion.vo.KeFuConversationVo;

import java.util.Collection;
import java.util.List;

/**
 * 客服会话 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IKeFuConversationService {

    /**
     * 查询客服会话详情。
     */
    KeFuConversationVo queryById(Long id);

    /**
     * 分页查询客服会话。
     */
    PageVo<KeFuConversationVo> queryPageList(KeFuConversationBo bo, PageBo pageBo);

    /**
     * 查询客服会话列表。
     */
    List<KeFuConversationVo> queryList(KeFuConversationBo bo);

    /**
     * 新增或修改客服会话。
     */
    Boolean saveOrUpdate(KeFuConversationBo bo);

    /**
     * 校验并批量删除客服会话。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
