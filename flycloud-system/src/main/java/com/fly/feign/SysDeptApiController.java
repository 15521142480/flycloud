package com.fly.feign;

import com.fly.common.domain.model.R;
import com.fly.system.api.domain.vo.SysDeptVo;
import com.fly.system.api.feign.ISysDeptApi;
import com.fly.system.service.ISysDeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * 系统内部接口-部门-控制层
 *
 * @author: lxs
 * @date: 2024/12/1
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SysDeptApiController implements ISysDeptApi {


    private final ISysDeptService sysDeptService;



    /**
     * 根据id获取部门信息
     *
     */
    @Override
    public R<SysDeptVo> getDeptById(Long id) {

        return R.ok(sysDeptService.queryById(id));
    }


    /**
     * 根据ids获取部门列表
     *
     */
    @Override
    public R<List<SysDeptVo>> getDeptListByIds(Collection<Long> ids) {

        return R.ok(sysDeptService.queryListByIds(ids));
    }


    /**
     * 根据ids验证部门
     *
     */
    @Override
    public R<Boolean> validateDeptByIds(Collection<Long> ids) {

        return R.ok(sysDeptService.validateDeptByIds(ids));
    }


}
