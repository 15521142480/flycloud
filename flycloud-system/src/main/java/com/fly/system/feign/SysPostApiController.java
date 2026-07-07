package com.fly.system.feign;

import com.fly.common.domain.model.R;
import com.fly.system.api.constants.SystemFeignApiConstants;
import com.fly.system.api.system.feign.ISysPostApi;
import com.fly.system.service.ISysPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * 系统内部接口-岗位-控制层
 *
 * @author: lxs
 * @date: 2026/07/08
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class SysPostApiController implements ISysPostApi {


    private final ISysPostService sysPostService;



    /**
     * 根据id获取信息
     *
     */
//    @Override
//    public R<SysPostVo> getPostById(Long id) {
//
//        return R.ok(sysPostService.queryById(id));
//    }


    /**
     * 根据ids获取列表
     *
     */
//    @Override
//    @GetMapping(SystemFeignApiConstants.xxx)
//    public R<List<SysPostVo>> getPostListByIds(Collection<Long> ids) {
//
//        return R.ok(sysPostService.queryListByIds(ids));
//    }


    /**
     * 根据ids验证
     *
     */
    @Override
    @GetMapping(SystemFeignApiConstants.PROVIDER_POST_VALID_IDS)
    public R<Boolean> validatePostByIds(Collection<Long> ids) {

        return R.result(sysPostService.validatePostByIds(ids));
    }


}
