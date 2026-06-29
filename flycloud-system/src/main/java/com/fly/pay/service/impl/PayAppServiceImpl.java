package com.fly.pay.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.common.domain.bo.PageBo;
import com.fly.common.domain.vo.PageVo;
import com.fly.common.exception.ServiceException;
import com.fly.common.security.util.UserUtils;
import com.fly.common.utils.StringUtils;
import com.fly.pay.mapper.PayAppMapper;
import com.fly.pay.service.IPayAppService;
import com.fly.system.api.pay.domain.PayApp;
import com.fly.system.api.pay.domain.bo.PayAppBo;
import com.fly.system.api.pay.domain.vo.PayAppVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付应用 Service 业务层处理。
 *
 * @author lxs
 * @date 2026-06-30
 */
@RequiredArgsConstructor
@Service
public class PayAppServiceImpl implements IPayAppService {

    private final PayAppMapper payAppMapper;

    @Override
    public PageVo<PayAppVo> queryPageList(PayAppBo bo, PageBo pageBo) {
        Page<PayAppVo> page = payAppMapper.selectVoPage(pageBo.build(), buildQueryWrapper(bo).orderByDesc(PayApp::getId));
        PageVo<PayAppVo> pageVo = new PageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        return pageVo;
    }

    @Override
    public List<PayAppVo> queryList(PayAppBo bo) {
        return payAppMapper.selectVoList(buildQueryWrapper(bo).orderByDesc(PayApp::getId));
    }

    @Override
    public PayAppVo queryById(Long id) {
        return payAppMapper.selectVoById(id);
    }

    @Override
    public Boolean saveOrUpdate(PayAppBo bo) {
        validateAppKeyUnique(bo.getId(), bo.getAppKey());
        PayApp app = BeanUtil.toBean(bo, PayApp.class);
        LocalDateTime now = LocalDateTime.now();
        String userId = String.valueOf(UserUtils.getCurUserId());
        app.setUpdateBy(userId);
        app.setUpdateTime(now);
        if (app.getId() != null) {
            validateExists(app.getId());
            return payAppMapper.updateById(app) > 0;
        }
        app.setIsDeleted(false);
        app.setCreateBy(userId);
        app.setCreateTime(now);
        return payAppMapper.insert(app) > 0;
    }

    @Override
    public Boolean updateStatus(Long id, Integer status) {
        validateExists(id);
        PayApp app = new PayApp();
        app.setId(id);
        app.setStatus(status);
        app.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        app.setUpdateTime(LocalDateTime.now());
        return payAppMapper.updateById(app) > 0;
    }

    @Override
    public Boolean deleteById(Long id) {
        validateExists(id);
        PayApp app = new PayApp();
        app.setId(id);
        app.setIsDeleted(true);
        app.setUpdateBy(String.valueOf(UserUtils.getCurUserId()));
        app.setUpdateTime(LocalDateTime.now());
        return payAppMapper.updateById(app) > 0;
    }

    private LambdaQueryWrapper<PayApp> buildQueryWrapper(PayAppBo bo) {
        LambdaQueryWrapper<PayApp> lqw = Wrappers.lambdaQuery();
        lqw.eq(PayApp::getIsDeleted, false);
        if (bo == null) {
            return lqw;
        }
        lqw.eq(bo.getId() != null, PayApp::getId, bo.getId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), PayApp::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getAppKey()), PayApp::getAppKey, bo.getAppKey());
        lqw.eq(bo.getStatus() != null, PayApp::getStatus, bo.getStatus());
        return lqw;
    }

    private void validateExists(Long id) {
        PayApp app = payAppMapper.selectById(id);
        if (app == null || Boolean.TRUE.equals(app.getIsDeleted())) {
            throw new ServiceException("支付应用不存在");
        }
    }

    private void validateAppKeyUnique(Long id, String appKey) {
        if (StringUtils.isBlank(appKey)) {
            return;
        }
        PayApp app = payAppMapper.selectOne(Wrappers.<PayApp>lambdaQuery()
                .eq(PayApp::getIsDeleted, false)
                .eq(PayApp::getAppKey, appKey)
                .last("LIMIT 1"));
        if (app != null && !app.getId().equals(id)) {
            throw new ServiceException("支付应用标识已存在");
        }
    }

}
