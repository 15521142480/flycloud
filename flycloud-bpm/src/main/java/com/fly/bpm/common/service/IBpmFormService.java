package com.fly.bpm.common.service;

import com.fly.bpm.api.domain.BpmForm;
import com.fly.bpm.api.domain.vo.BpmFormVo;
import com.fly.bpm.api.domain.bo.BpmFormBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.utils.collection.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * BPM 单定义Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmFormService {

    /**
     * 查询BPM 单定义
     */
    BpmFormVo queryById(Long id);

    /**
     * 获得动态表单列表
     */
    List<BpmForm> queryListByIds(Collection<Long> ids);

    /**
     * 查询BPM 单定义列表
     */
    PageVo<BpmFormVo> queryPageList(BpmFormBo bo, PageBo pageBo);

    /**
     * 查询BPM 单定义列表
     */
    List<BpmFormVo> queryList(BpmFormBo bo);

    /**
     * 修改BPM 单定义
     */
    Boolean insertByBo(BpmFormBo bo);

    /**
     * 修改BPM 单定义
     */
    Boolean updateByBo(BpmFormBo bo);

    /**
     * 校验并批量删除BPM 单定义信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 获得动态表单 Map
     *
     * @param ids 编号
     * @return 动态表单 Map
     */
    default Map<Long, BpmForm> getFormMap(Collection<Long> ids) {
        return CollectionUtils.convertMap(this.queryListByIds(ids), BpmForm::getId);
    }

}
