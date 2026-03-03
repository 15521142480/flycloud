package com.fly.bpm.oa.service;

import com.fly.bpm.api.domain.bo.BpmOALeaveCreateBo;
import com.fly.bpm.api.domain.vo.BpmOaLeaveVo;
import com.fly.bpm.api.domain.bo.BpmOaLeaveBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;

import java.util.Collection;
import java.util.List;

/**
 * OA 请假申请Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmOaLeaveService {

    /**
     * 查询OA 请假申请
     */
    BpmOaLeaveVo queryById(Long id);

    /**
     * 查询OA 请假申请列表
     */
    PageVo<BpmOaLeaveVo> queryPageList(BpmOaLeaveBo bo, PageBo pageBo);

    /**
     * 查询OA 请假申请列表
     */
    List<BpmOaLeaveVo> queryList(BpmOaLeaveBo bo);

    /**
     * 新增OA 请假申请
     */
    Boolean insertByBo(BpmOALeaveCreateBo bo);

    /**
     * 修改OA 请假申请
     */
    Boolean updateByBo(BpmOaLeaveBo bo);

    /**
     * 更新请假申请的状态
     *
     * @param id 编号
     * @param status 结果
     */
    void updateLeaveStatus(Long id, Integer status);

    /**
     * 校验并批量删除OA 请假申请信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
