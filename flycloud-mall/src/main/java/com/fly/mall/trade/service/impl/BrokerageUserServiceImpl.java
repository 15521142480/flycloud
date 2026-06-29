package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.mall.api.trade.domain.BrokerageUser;
import com.fly.mall.api.trade.domain.bo.BrokerageUserBo;
import com.fly.mall.api.trade.domain.vo.BrokerageUserVo;
import com.fly.mall.trade.mapper.BrokerageUserMapper;
import com.fly.mall.trade.service.IBrokerageUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 分销用户 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class BrokerageUserServiceImpl extends BaseServiceImpl<BrokerageUserMapper, BrokerageUser> implements IBrokerageUserService {

    private final BrokerageUserMapper baseMapper;

    /**
     * 查询分销用户详情。
     */
    @Override
    public BrokerageUserVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询分销用户。
     */
    @Override
    public PageVo<BrokerageUserVo> queryPageList(BrokerageUserBo bo, PageBo pageBo) {
        LambdaQueryWrapper<BrokerageUser> lqw = buildQueryWrapper(bo);
        Page<BrokerageUserVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询分销用户列表。
     */
    @Override
    public List<BrokerageUserVo> queryList(BrokerageUserBo bo) {
        LambdaQueryWrapper<BrokerageUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改分销用户。
     */
    @Override
    public Boolean saveOrUpdate(BrokerageUserBo bo) {
        BrokerageUser entity = BeanUtil.toBean(bo, BrokerageUser.class);
        boolean isUpdate = entity.getId() != null;
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        entity.setUpdateBy(userId);
        entity.setUpdateTime(now);
        if (entity.getIsDeleted() == null) {
            entity.setIsDeleted(false);
        }
        if (isUpdate) {
            return baseMapper.updateById(entity) > 0;
        }
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return baseMapper.insert(entity) > 0;
    }

    /**
     * 校验并批量删除分销用户。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            BrokerageUser entity = new BrokerageUser();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建分销用户查询条件。
     */
    private LambdaQueryWrapper<BrokerageUser> buildQueryWrapper(BrokerageUserBo bo) {
        LambdaQueryWrapper<BrokerageUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(BrokerageUser::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, BrokerageUser::getId, bo.getId());
        lqw.eq(bo.getBindUserId() != null, BrokerageUser::getBindUserId, bo.getBindUserId());
        lqw.eq(bo.getBindUserTime() != null, BrokerageUser::getBindUserTime, bo.getBindUserTime());
        lqw.eq(bo.getBrokerageEnabled() != null, BrokerageUser::getBrokerageEnabled, bo.getBrokerageEnabled());
        lqw.eq(bo.getBrokerageTime() != null, BrokerageUser::getBrokerageTime, bo.getBrokerageTime());
        lqw.eq(bo.getBrokeragePrice() != null, BrokerageUser::getBrokeragePrice, bo.getBrokeragePrice());
        lqw.eq(bo.getFrozenPrice() != null, BrokerageUser::getFrozenPrice, bo.getFrozenPrice());
        return lqw;
    }

}
