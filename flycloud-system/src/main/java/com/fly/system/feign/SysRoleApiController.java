package com.fly.system.feign;

import com.fly.common.domain.model.R;
import com.fly.system.api.constants.SystemFeignApiConstants;
import com.fly.system.api.system.feign.ISysRoleApi;
import com.fly.system.service.ISysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;

/**
 * 系统内部接口-角色-控制层
 *
 * @author: lxs
 * @date: 2026/07/08
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SysRoleApiController implements ISysRoleApi {


    private final ISysRoleService sysRoleService;



    /**
     * 根据id获取信息
     *
     */
//    @Override
//    public R<SysRoleVo> getRoleById(Long id) {
//
//        return R.ok(sysRoleService.queryById(id));
//    }


    /**
     * 根据ids获取列表
     *
     */
//    @Override
//    public R<List<SysRoleVo>> getRoleListByIds(Collection<Long> ids) {
//
//        return R.ok(sysRoleService.queryListByIds(ids));
//    }


    /**
     * 根据ids验证
     *
     */
    @Override
    @GetMapping(SystemFeignApiConstants.PROVIDER_ROLE_VALID_IDS)
    public R<Boolean> validateRoleByIds(Set<Long> ids) {

        return R.result(sysRoleService.validateRoleByIds(ids));
    }


}
