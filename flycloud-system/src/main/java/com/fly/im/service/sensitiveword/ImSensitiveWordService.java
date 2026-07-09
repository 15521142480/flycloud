package com.fly.im.service.sensitiveword;

import com.fly.im.framework.pojo.PageResult;
import com.fly.system.api.im.domain.bo.ImSensitiveWordPageBo;
import com.fly.system.api.im.domain.bo.ImSensitiveWordBo;
import com.fly.system.api.im.domain.ImSensitiveWord;
import jakarta.validation.Valid;

import java.util.List;

/**
 * IM 敏感词 Service 接口
 *
 * @author lxs
 * @date 2026-07-02
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
    PageResult<ImSensitiveWord> getSensitiveWordPage(ImSensitiveWordPageBo reqVo);

    /**
     * 【管理后台】获取敏感词详情
     */
    ImSensitiveWord getSensitiveWord(Long id);

    /**
     * 【管理后台】新增敏感词，返回新增 id
     */
    Long createSensitiveWord(@Valid ImSensitiveWordBo reqVo);

    /**
     * 【管理后台】修改敏感词
     */
    void updateSensitiveWord(@Valid ImSensitiveWordBo reqVo);

    /**
     * 【管理后台】删除敏感词
     */
    void deleteSensitiveWord(Long id);

    /**
     * 【管理后台】批量删除敏感词
     */
    void deleteSensitiveWordList(List<Long> ids);

}
