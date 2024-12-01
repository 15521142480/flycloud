package com.fly.bpm.common.service;

import com.fly.bpm.api.domain.BpmCategory;
import com.fly.bpm.api.domain.vo.BpmCategoryVo;
import com.fly.bpm.api.domain.bo.BpmCategoryBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.utils.collection.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * BPM 流程分类Service接口
 *
 * @author fly
 * @date 2024-11-24
 */
public interface IBpmCategoryService {

    /**
     * 查询BPM 流程分类
     */
    BpmCategoryVo queryById(Long id);


    /**
     * 根据编码获得流程分类列表
     *
     * @return 流程分类列表
     */
    List<BpmCategory> queryCategoryListByCode(Collection<String> codes);


    /**
     * 流程分类分页列表
     */
    PageVo<BpmCategoryVo> queryPageList(BpmCategoryBo bo, PageBo pageBo);

    /**
     * 流程分类列表
     */
    List<BpmCategoryVo> queryList(BpmCategoryBo bo);

    /**
     * 修改BPM 流程分类
     */
    Boolean insertByBo(BpmCategoryBo bo);

    /**
     * 修改BPM 流程分类
     */
    Boolean updateByBo(BpmCategoryBo bo);

    /**
     * 校验并批量删除BPM 流程分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 获得流程分类 Map，基于指定编码
     *
     * @param codes 编号数组
     * @return 流程分类 Map
     */
    default Map<String, BpmCategory> getCategoryMap(Collection<String> codes) {
        return CollectionUtils.convertMap(this.queryCategoryListByCode(codes), BpmCategory::getCode);
    }
}
