package com.fly.im.framework.system;

import com.fly.common.domain.model.R;
import com.fly.system.api.system.domain.vo.SysUserVo;
import com.fly.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * IM 系统用户查询适配。
 *
 * @author lxs
 * @date 2026-06-30
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
        return R.ok(true);
    }

    public R<Boolean> validateUser(Long id) {
        return R.ok(id != null && sysUserService.queryById(id) != null);
    }

}
