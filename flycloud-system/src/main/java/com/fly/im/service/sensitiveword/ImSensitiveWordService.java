package com.fly.im.service.sensitiveword;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.controller.admin.manager.sensitiveword.vo.ImSensitiveWordPageReqVO;
import com.fly.im.controller.admin.manager.sensitiveword.vo.ImSensitiveWordSaveReqVO;
import com.fly.im.dal.dataobject.sensitiveword.ImSensitiveWordDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * IM 敏感词 Service 接口
 *
 * @author lxs
 * @date 2026-06-30
 */
public interface ImSensitiveWordService {

    /**
     * 校验文本是否包含敏感词
     *
     * @param text 待校验文本
     */
    void validateText(String text);

    // ==================== 管理后台 ====================

    /**
     * 【管理后台】分页查询敏感词
     */
    PageResult<ImSensitiveWordDO> getSensitiveWordPage(ImSensitiveWordPageReqVO reqVO);

    /**
     * 【管理后台】获取敏感词详情
     */
    ImSensitiveWordDO getSensitiveWord(Long id);

    /**
     * 【管理后台】新增敏感词，返回新增 id
     */
    Long createSensitiveWord(@Valid ImSensitiveWordSaveReqVO reqVO);

    /**
     * 【管理后台】修改敏感词
     */
    void updateSensitiveWord(@Valid ImSensitiveWordSaveReqVO reqVO);

    /**
     * 【管理后台】删除敏感词
     */
    void deleteSensitiveWord(Long id);

    /**
     * 【管理后台】批量删除敏感词
     */
    void deleteSensitiveWordList(List<Long> ids);

}
