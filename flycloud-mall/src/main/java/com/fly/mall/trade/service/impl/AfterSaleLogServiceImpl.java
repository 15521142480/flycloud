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
import com.fly.mall.api.domain.trade.AfterSaleLog;
import com.fly.mall.api.domain.trade.bo.AfterSaleLogBo;
import com.fly.mall.api.domain.trade.vo.AfterSaleLogVo;
import com.fly.mall.trade.mapper.AfterSaleLogMapper;
import com.fly.mall.trade.service.IAfterSaleLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * 售后日志 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-28
 */
@RequiredArgsConstructor
@Service
public class AfterSaleLogServiceImpl extends BaseServiceImpl<AfterSaleLogMapper, AfterSaleLog> implements IAfterSaleLogService {

    private final AfterSaleLogMapper baseMapper;

    /**
     * 查询售后日志详情。
     */
    @Override
    public AfterSaleLogVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询售后日志。
     */
    @Override
    public PageVo<AfterSaleLogVo> queryPageList(AfterSaleLogBo bo, PageBo pageBo) {
        LambdaQueryWrapper<AfterSaleLog> lqw = buildQueryWrapper(bo);
        Page<AfterSaleLogVo> result = baseMapper.selectVoPage(pageBo.build(), lqw);
        return this.build(result);
    }

    /**
     * 查询售后日志列表。
     */
    @Override
    public List<AfterSaleLogVo> queryList(AfterSaleLogBo bo) {
        LambdaQueryWrapper<AfterSaleLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增或修改售后日志。
     */
    @Override
    public Boolean saveOrUpdate(AfterSaleLogBo bo) {
        AfterSaleLog entity = BeanUtil.toBean(bo, AfterSaleLog.class);
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
     * 校验并批量删除售后日志。
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        for (Long id : ids) {
            AfterSaleLog entity = new AfterSaleLog();
            entity.setId(id);
            entity.setIsDeleted(true);
            entity.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
            entity.setUpdateTime(LocalDateTime.now());
            baseMapper.updateById(entity);
        }
        return true;
    }

    /**
     * 构建售后日志查询条件。
     */
    private LambdaQueryWrapper<AfterSaleLog> buildQueryWrapper(AfterSaleLogBo bo) {
        LambdaQueryWrapper<AfterSaleLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(AfterSaleLog::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, AfterSaleLog::getId, bo.getId());
        lqw.eq(bo.getUserId() != null, AfterSaleLog::getUserId, bo.getUserId());
        lqw.eq(bo.getUserType() != null, AfterSaleLog::getUserType, bo.getUserType());
        lqw.eq(bo.getAfterSaleId() != null, AfterSaleLog::getAfterSaleId, bo.getAfterSaleId());
        lqw.eq(bo.getBeforeStatus() != null, AfterSaleLog::getBeforeStatus, bo.getBeforeStatus());
        lqw.eq(bo.getAfterStatus() != null, AfterSaleLog::getAfterStatus, bo.getAfterStatus());
        lqw.eq(bo.getOperateType() != null, AfterSaleLog::getOperateType, bo.getOperateType());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), AfterSaleLog::getContent, bo.getContent());
        return lqw;
    }

}
