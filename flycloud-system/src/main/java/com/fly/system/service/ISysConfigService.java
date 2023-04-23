package com.fly.system.service;

import com.fly.common.database.web.domain.bo.PageBo;
import com.fly.common.database.web.domain.vo.PageVo;
import com.fly.system.domain.bo.SysConfigBo;
import com.fly.system.domain.vo.SysConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 参数配置Service接口
 *
 * @author fly
 * @date 2023-04-23
 */
public interface ISysConfigService {

    /**
     * 查询参数配置
     */
    SysConfigVo queryById(Long configId);

    /**
     * 查询参数配置列表
     */
    PageVo<SysConfigVo> queryPageList(SysConfigBo bo, PageBo pageBo);

    /**
     * 查询参数配置列表
     */
    List<SysConfigVo> queryList(SysConfigBo bo);

    /**
     * 修改参数配置
     */
    Boolean insertByBo(SysConfigBo bo);

    /**
     * 修改参数配置
     */
    Boolean updateByBo(SysConfigBo bo);

    /**
     * 校验并批量删除参数配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
