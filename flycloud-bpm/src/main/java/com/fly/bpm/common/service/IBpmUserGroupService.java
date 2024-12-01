package com.fly.bpm.common.service;

import com.fly.bpm.api.domain.vo.BpmUserGroupVo;
import com.fly.bpm.api.domain.bo.BpmUserGroupBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * BPM 用户组Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmUserGroupService {

    /**
     * 查询BPM 用户组
     */
    BpmUserGroupVo queryById(Long id);

    /**
     * 查询BPM 用户组列表
     */
    PageVo<BpmUserGroupVo> queryPageList(BpmUserGroupBo bo, PageBo pageBo);

    /**
     * 查询BPM 用户组列表
     */
    List<BpmUserGroupVo> queryList(BpmUserGroupBo bo);

    /**
     * 修改BPM 用户组
     */
    Boolean insertByBo(BpmUserGroupBo bo);

    /**
     * 修改BPM 用户组
     */
    Boolean updateByBo(BpmUserGroupBo bo);

    /**
     * 校验并批量删除BPM 用户组信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
