package com.fly.im.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.im.framework.pojo.PageResult;
import com.fly.common.database.web.query.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.bo.ImSensitiveWordPageBo;
import com.fly.system.api.im.domain.ImSensitiveWord;
import com.fly.system.api.im.domain.vo.ImSensitiveWordVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IM 敏感词 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface ImSensitiveWordMapper extends BaseMapperPlus<ImSensitiveWordMapper, ImSensitiveWord, ImSensitiveWordVo> {

    default List<ImSensitiveWord> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImSensitiveWord>()
                .eq(ImSensitiveWord::getStatus, status));
    }

    default ImSensitiveWord selectByWord(String word) {
        return selectOne(new LambdaQueryWrapperX<ImSensitiveWord>()
                .eq(ImSensitiveWord::getWord, word));
    }

    default PageResult<ImSensitiveWord> selectPage(ImSensitiveWordPageBo reqVo) {
        return new PageResult<>(selectPage(reqVo.build(), new LambdaQueryWrapperX<ImSensitiveWord>()
                .likeIfPresent(ImSensitiveWord::getWord, reqVo.getWord())
                .eqIfPresent(ImSensitiveWord::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImSensitiveWord::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImSensitiveWord::getId)));
    }

    @Select("SELECT MAX(update_time) FROM im_sensitive_word")
    LocalDateTime selectMaxUpdateTime(@Param("tenantId") Long tenantId);

}
