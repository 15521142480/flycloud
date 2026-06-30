package com.fly.im.dal.mysql.channel;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.im.controller.admin.manager.channel.vo.channel.ImChannelPageReqVo;
import com.fly.im.dal.dataobject.channel.ImChannelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 频道 Mapper
 *
 * @author lxs
 * @date 2026-06-30
 */
@Mapper
public interface ImChannelMapper extends BaseMapperX<ImChannelDO> {

    default ImChannelDO selectByCode(String code) {
        return selectOne(ImChannelDO::getCode, code);
    }

    default List<ImChannelDO> selectListByStatusOrderBySort(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImChannelDO>()
                .eq(ImChannelDO::getStatus, status)
                .orderByAsc(ImChannelDO::getSort));
    }

    default PageResult<ImChannelDO> selectPage(ImChannelPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImChannelDO>()
                .likeIfPresent(ImChannelDO::getCode, reqVo.getCode())
                .likeIfPresent(ImChannelDO::getName, reqVo.getName())
                .eqIfPresent(ImChannelDO::getStatus, reqVo.getStatus())
                .orderByAsc(ImChannelDO::getSort)
                .orderByDesc(ImChannelDO::getId));
    }

}
