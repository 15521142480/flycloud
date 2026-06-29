package com.fly.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fly.common.security.util.UserUtils;
import com.fly.member.mapper.MemberConfigMapper;
import com.fly.member.service.IMemberConfigService;
import com.fly.system.api.member.domain.MemberConfig;
import com.fly.system.api.member.domain.bo.MemberConfigBo;
import com.fly.system.api.member.domain.vo.MemberConfigVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 会员配置 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class MemberConfigServiceImpl implements IMemberConfigService {

    private final MemberConfigMapper memberConfigMapper;

    /**
     * 保存会员配置，不存在则新增，存在则更新第一条配置。
     */
    @Override
    public Boolean saveConfig(MemberConfigBo bo) {
        MemberConfig config = BeanUtil.toBean(bo, MemberConfig.class);
        MemberConfigVo oldConfig = getConfig();
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        config.setUpdateBy(userId);
        config.setUpdateTime(now);
        if (oldConfig != null) {
            config.setId(oldConfig.getId());
            return memberConfigMapper.updateById(config) > 0;
        }
        config.setIsDeleted(false);
        config.setCreateBy(userId);
        config.setCreateTime(now);
        return memberConfigMapper.insert(config) > 0;
    }

    /**
     * 查询会员配置。
     */
    @Override
    public MemberConfigVo getConfig() {
        LambdaQueryWrapper<MemberConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberConfig::getIsDeleted, false);
        lqw.orderByAsc(MemberConfig::getId);
        lqw.last("LIMIT 1");
        return memberConfigMapper.selectVoOne(lqw);
    }

}
