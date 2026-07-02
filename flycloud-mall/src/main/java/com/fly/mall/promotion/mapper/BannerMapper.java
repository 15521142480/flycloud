package com.fly.mall.promotion.mapper;

import com.fly.common.database.web.support.BaseMapperPlus;
import com.fly.mall.api.promotion.domain.Banner;
import com.fly.mall.api.promotion.domain.vo.BannerVo;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

/**
 * 轮播图 Mapper 接口。
 *
 * @author lxs
 * @date 2026-07-02
 */
public interface BannerMapper extends BaseMapperPlus<BannerMapper, Banner, BannerVo> {

    /**
     * 增加轮播图浏览次数。
     */
    default void updateBrowseCount(Long id) {
        update(null, new LambdaUpdateWrapper<Banner>()
                .eq(Banner::getId, id)
                .setSql("browse_count = browse_count + 1"));
    }

}
