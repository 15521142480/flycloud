package com.fly.im.dal.mysql.sensitiveword;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.sensitiveword.vo.ImSensitiveWordPageReqVO;
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

    default PageResult<ImSensitiveWordDO> selectPage(ImSensitiveWordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ImSensitiveWordDO>()
                .likeIfPresent(ImSensitiveWordDO::getWord, reqVO.getWord())
                .eqIfPresent(ImSensitiveWordDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ImSensitiveWordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ImSensitiveWordDO::getId));
    }

    @Select("SELECT MAX(update_time) FROM im_sensitive_word")
    LocalDateTime selectMaxUpdateTime(@Param("tenantId") Long tenantId);

}
