package com.fly.mall.promotion.service;

import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.mall.api.promotion.domain.bo.BargainRecordBo;
import com.fly.mall.api.promotion.domain.vo.BargainRecordVo;

import java.util.Collection;
import java.util.List;

/**
 * 砍价记录 Service 接口。
 *
 * @author lxs
 * @date 2026-06-28
 */
public interface IBargainRecordService {

    /**
     * 查询砍价记录详情。
     */
    BargainRecordVo queryById(Long id);

    /**
     * 分页查询砍价记录。
     */
    PageVo<BargainRecordVo> queryPageList(BargainRecordBo bo, PageBo pageBo);

    /**
     * 查询砍价记录列表。
     */
    List<BargainRecordVo> queryList(BargainRecordBo bo);

    /**
     * 新增或修改砍价记录。
     */
    Boolean saveOrUpdate(BargainRecordBo bo);

    /**
     * 校验并批量删除砍价记录。
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
