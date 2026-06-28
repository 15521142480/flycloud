package com.fly.mall.trade.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.domain.trade.bo.BrokerageUserBo;
import com.fly.mall.api.domain.trade.vo.BrokerageUserVo;

import java.util.Collection;
import java.util.List;

/**
 * 分销用户 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IBrokerageUserService {

    /**
     * 查询分销用户详情。
     */
    BrokerageUserVo queryById(Long id);

    /**
     * 分页查询分销用户。
     */
    PageVo<BrokerageUserVo> queryPageList(BrokerageUserBo bo, PageBo pageBo);

    /**
     * 查询分销用户列表。
     */
    List<BrokerageUserVo> queryList(BrokerageUserBo bo);

    /**
     * 新增或修改分销用户。
     */
    Boolean saveOrUpdate(BrokerageUserBo bo);

    /**
     * 校验并批量删除分销用户。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
