package com.fly.im.dal.mysql.channel;

import com.fly.im.framework.pojo.PageResult;
import com.fly.im.framework.mybatis.BaseMapperX;
import com.fly.im.framework.mybatis.LambdaQueryWrapperX;
import com.fly.system.api.im.domain.vo.admin.manager.channel.channel.ImChannelPageReqVo;
import com.fly.system.api.im.domain.channel.ImChannel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * IM 频道 Mapper
 *
 * @author lxs
 * @date 2026-07-02
 */
@Mapper
public interface ImChannelMapper extends BaseMapperX<ImChannel> {

    default ImChannel selectByCode(String code) {
        return selectOne(ImChannel::getCode, code);
    }

    default List<ImChannel> selectListByStatusOrderBySort(Integer status) {
        return selectList(new LambdaQueryWrapperX<ImChannel>()
                .eq(ImChannel::getStatus, status)
                .orderByAsc(ImChannel::getSort));
    }

    default PageResult<ImChannel> selectPage(ImChannelPageReqVo reqVo) {
        return selectPage(reqVo, new LambdaQueryWrapperX<ImChannel>()
                .likeIfPresent(ImChannel::getCode, reqVo.getCode())
                .likeIfPresent(ImChannel::getName, reqVo.getName())
                .eqIfPresent(ImChannel::getStatus, reqVo.getStatus())
                .orderByAsc(ImChannel::getSort)
                .orderByDesc(ImChannel::getId));
    }

}
