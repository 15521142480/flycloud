package com.fly.system.api.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.common.utils.collection.CollectionUtils;
import com.fly.system.api.constants.SystemFeignApiConstants;
import com.fly.system.api.domain.vo.SysDeptVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 部门调用类
 * <p>
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "SysDeptApi")
public interface ISysDeptApi {


    /**
     * 根据id获取部门信息
     * @param id　部门id
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_DEPT_ID)
    R<SysDeptVo> getDeptById(@RequestParam("id") Long id);


    /**
     * 根据ids获取部门列表
     * @param ids　部门ids
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_DEPT_IDS)
    R<List<SysDeptVo>> getDeptListByIds(@RequestParam("ids") Collection<Long> ids);


    /**
     * 根据ids验证部门
     * @param ids　部门ids
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_DEPT_VALID_IDS)
    R<Boolean> validateDeptByIds(@RequestParam("ids") Collection<Long> ids);



    // ==========================================================================

    /**
     * 获得用户 Map
     *
     * @param ids 用户编号数组
     * @return 用户 Map
     */
    default Map<Long, SysDeptVo> getDeptMapByIds(Collection<Long> ids) {

        List<SysDeptVo> users = this.getDeptListByIds(ids).getCheckedData();
        return CollectionUtils.convertMap(users, SysDeptVo::getId);
    }

}
