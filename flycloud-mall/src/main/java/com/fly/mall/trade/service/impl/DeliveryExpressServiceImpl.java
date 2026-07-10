package com.fly.mall.trade.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.database.web.service.impl.BaseServiceImpl;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.mall.api.trade.domain.DeliveryExpress;
import com.fly.mall.api.trade.domain.bo.DeliveryExpressBo;
import com.fly.mall.api.trade.domain.vo.DeliveryExpressVo;
import com.fly.mall.trade.mapper.DeliveryExpressMapper;
import com.fly.mall.trade.service.IDeliveryExpressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 快递公司 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-07-02
 */
@RequiredArgsConstructor
@Service
public class DeliveryExpressServiceImpl extends BaseServiceImpl<DeliveryExpressMapper, DeliveryExpress> implements IDeliveryExpressService {

    private final DeliveryExpressMapper baseMapper;

    /**
     * 查询快递公司详情。
     */
    @Override
    public DeliveryExpressVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询快递公司。
     */
    @Override
    public PageVo<DeliveryExpressVo> queryPageList(DeliveryExpressBo bo, PageBo pageBo) {
        LambdaQueryWrapper<DeliveryExpress> lqw = buildQueryWrapper(bo);
        Page<DeliveryExpressVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询快递公司列表。
     */
    @Override
    public List<DeliveryExpressVo> queryList(DeliveryExpressBo bo) {
        LambdaQueryWrapper<DeliveryExpress> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改快递公司。
     */
    @Override
    public Boolean saveOrUpdate(DeliveryExpressBo bo) {
        DeliveryExpress entity = BeanUtil.toBean(bo, DeliveryExpress.class);
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
     * 校验并批量删除快递公司。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        
        return baseMapper.deleteByIds(ids) > 0;
    }

    /**
     * 构建快递公司查询条件。
     */
    private LambdaQueryWrapper<DeliveryExpress> buildQueryWrapper(DeliveryExpressBo bo) {
        LambdaQueryWrapper<DeliveryExpress> lqw = Wrappers.lambdaQuery();
        lqw.eq(DeliveryExpress::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, DeliveryExpress::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getCode()), DeliveryExpress::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DeliveryExpress::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getLogo()), DeliveryExpress::getLogo, bo.getLogo());
        lqw.eq(bo.getSort() != null, DeliveryExpress::getSort, bo.getSort());
        lqw.eq(bo.getStatus() != null, DeliveryExpress::getStatus, bo.getStatus());
        return lqw;
    }

}
