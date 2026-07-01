package com.fly.im.framework.system;

import cn.hutool.core.collection.CollUtil;
import com.fly.common.domain.model.R;
import com.fly.common.enums.ErrorCodeConstants;
import com.fly.common.enums.StatusEnum;
import com.fly.common.exception.utils.ServiceExceptionUtils;
import com.fly.system.api.system.domain.vo.SysUserVo;
import com.fly.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * IM 系统用户查询适配。
 *
 * @author lxs
 * @date 2026-07-02
 */
@Component
@RequiredArgsConstructor
public class AdminUserApi {

    private final ISysUserService sysUserService;

    public R<SysUserVo> getUser(Long id) {
        return R.ok(sysUserService.queryById(id));
    }

    public Map<Long, SysUserVo> getUserMap(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return sysUserService.getByIds(ids).stream().collect(Collectors.toMap(SysUserVo::getId, Function.identity(), (a, b) -> a));
    }

    public R<Boolean> validateUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return R.ok(false);
        }
        Map<Long, SysUserVo> userMap = getUserMap(ids);
        ids.forEach(id -> validateUserExistsAndEnable(id, userMap.get(id)));
        return R.ok(true);
    }

    public R<Boolean> validateUser(Long id) {
        validateUserExistsAndEnable(id, id == null ? null : sysUserService.queryById(id));
        return R.ok(true);
    }

    /**
     * 校验系统用户存在并处于启用状态，避免 IM 业务写入无效用户关系。
     */
    private void validateUserExistsAndEnable(Long id, SysUserVo user) {
        if (id == null || user == null) {
            throw ServiceExceptionUtils.exception(ErrorCodeConstants.USER_NOT_EXISTS);
        }
        if (!Objects.equals(StatusEnum.ENABLE.getStatus(), user.getStatus())) {
            throw ServiceExceptionUtils.exception(ErrorCodeConstants.USER_IS_DISABLE, user.getName());
        }
    }

}
