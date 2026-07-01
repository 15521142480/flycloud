package com.fly.member.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.member.mapper.MemberSignInConfigMapper;
import com.fly.member.service.IMemberSignInConfigService;
import com.fly.system.api.member.domain.MemberSignInConfig;
import com.fly.system.api.member.domain.bo.MemberSignInConfigBo;
import com.fly.system.api.member.domain.vo.MemberSignInConfigVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员签到规则 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class MemberSignInConfigServiceImpl implements IMemberSignInConfigService {

    private final MemberSignInConfigMapper signInConfigMapper;

    @Override
    public PageVo<MemberSignInConfigVo> queryPageList(MemberSignInConfigBo bo, PageBo pageBo) {
        LambdaQueryWrapper<MemberSignInConfig> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(MemberSignInConfig::getDay);
        Page<MemberSignInConfigVo> page = signInConfigMapper.selectVoPage(pageBo.build(), lqw);
        PageVo<MemberSignInConfigVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<MemberSignInConfigVo> queryList(MemberSignInConfigBo bo) {
        LambdaQueryWrapper<MemberSignInConfig> lqw = buildQueryWrapper(bo);
        lqw.orderByAsc(MemberSignInConfig::getDay);
        return signInConfigMapper.selectVoList(lqw);
    }

    @Override
    public MemberSignInConfigVo queryById(Long id) {
        return signInConfigMapper.selectVoById(id);
    }

    @Override
    public Long createSignInConfig(MemberSignInConfigBo bo) {
        validateDayUnique(null, bo.getDay());
        MemberSignInConfig config = BeanUtil.toBean(bo, MemberSignInConfig.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        config.setIsDeleted(false);
        config.setCreateBy(userId);
        config.setCreateTime(now);
        config.setUpdateBy(userId);
        config.setUpdateTime(now);
        signInConfigMapper.insert(config);
        return config.getId();
    }

    @Override
    public Boolean saveOrUpdate(MemberSignInConfigBo bo) {
        validateDayUnique(bo.getId(), bo.getDay());
        MemberSignInConfig config = BeanUtil.toBean(bo, MemberSignInConfig.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        config.setUpdateBy(userId);
        config.setUpdateTime(now);
        if (config.getId() != null) {
            validateExists(config.getId());
            return signInConfigMapper.updateById(config) > 0;
        }
        config.setIsDeleted(false);
        config.setCreateBy(userId);
        config.setCreateTime(now);
        return signInConfigMapper.insert(config) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        validateExists(id);
        MemberSignInConfig config = new MemberSignInConfig();
        config.setId(id);
        config.setIsDeleted(true);
        config.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        config.setUpdateTime(LocalDateTime.now());
        return signInConfigMapper.updateById(config) > 0;
    }

    private LambdaQueryWrapper<MemberSignInConfig> buildQueryWrapper(MemberSignInConfigBo bo) {
        LambdaQueryWrapper<MemberSignInConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberSignInConfig::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, MemberSignInConfig::getId, bo.getId());
        lqw.eq(bo.getDay() != null, MemberSignInConfig::getDay, bo.getDay());
        lqw.eq(bo.getStatus() != null, MemberSignInConfig::getStatus, bo.getStatus());
        return lqw;
    }

    private void validateExists(Long id) {
        MemberSignInConfig config = signInConfigMapper.selectById(id);
        if (config == null || Boolean.TRUE.equals(config.getIsDeleted())) {
            throw new ServiceException("会员签到规则不存在");
        }
    }

    private void validateDayUnique(Long id, Integer day) {
        if (day == null) {
            return;
        }
        LambdaQueryWrapper<MemberSignInConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(MemberSignInConfig::getIsDeleted, false);
        lqw.eq(MemberSignInConfig::getDay, day);
        lqw.last("LIMIT 1");
        MemberSignInConfig config = signInConfigMapper.selectOne(lqw);
        if (config != null && !config.getId().equals(id)) {
            throw new ServiceException("签到天数规则已存在");
        }
    }

}
