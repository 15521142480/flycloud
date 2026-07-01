package com.fly.im.dal.mysql.sensitiveword;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.sensitiveword.ImSensitiveWordPageReqVo;
import com.fly.system.api.im.domain.sensitiveword.ImSensitiveWord;
import org.apache.ibatis.annotations.Mapper;
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
@Mapper
public interface ImSensitiveWordMapper extends BaseMapperX<ImSensitiveWord> {

    default List<ImSensitiveWord> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImSensitiveWord>()
                .eq(ImSensitiveWord::getStatus, status));
    }

    default ImSensitiveWord selectByWord(String word) {
        return selectOne(new LambdaQueryWrapperX<ImSensitiveWord>()
                .eq(ImSensitiveWord::getWord, word));
    }

    default PageResult<ImSensitiveWord> selectPage(ImSensitiveWordPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImSensitiveWord>()
                .likeIfPresent(ImSensitiveWord::getWord, reqVo.getWord())
                .eqIfPresent(ImSensitiveWord::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImSensitiveWord::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImSensitiveWord::getId));
    }

    @Select("SELECT MAX(update_time) FROM im_sensitive_word")
    LocalDateTime selectMaxUpdateTime(@Param("tenantId") Long tenantId);

}
