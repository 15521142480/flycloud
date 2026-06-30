package com.fly.im.dal.mysql.sensitiveword;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.sensitiveword.vo.ImSensitiveWordPageReqVo;
import com.fly.im.dal.dataobject.sensitiveword.ImSensitiveWordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * IM 敏感词 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImSensitiveWordMapper extends BaseMapperX<ImSensitiveWordDO> {

    default List<ImSensitiveWordDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImSensitiveWordDO>()
                .eq(ImSensitiveWordDO::getStatus, status));
    }

    default ImSensitiveWordDO selectByWord(String word) {
        return selectOne(new LambdaQueryWrapperX<ImSensitiveWordDO>()
                .eq(ImSensitiveWordDO::getWord, word));
    }

    default PageResult<ImSensitiveWordDO> selectPage(ImSensitiveWordPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImSensitiveWordDO>()
                .likeIfPresent(ImSensitiveWordDO::getWord, reqVo.getWord())
                .eqIfPresent(ImSensitiveWordDO::getStatus, reqVo.getStatus())
                .betweenIfPresent(ImSensitiveWordDO::getCreateTime, reqVo.getCreateTime())
                .orderByDesc(ImSensitiveWordDO::getId));
    }

    @Select("SELECT MAX(update_time) FROM im_sensitive_word")
    LocalDateTime selectMaxUpdateTime(@Param("tenantId") Long tenantId);

}
