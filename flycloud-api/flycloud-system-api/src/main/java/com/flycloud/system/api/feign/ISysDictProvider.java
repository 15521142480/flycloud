package com.flycloud.system.api.feign;

import com.fly.common.constant.ProviderConstants;
import com.fly.common.constant.ServerNames;
import com.fly.common.model.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.flycloud.system.api.entity.SysDict;

import java.util.List;

/**
 * 字典远程调用接口类
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "SysDictProvider")
public interface ISysDictProvider {

    /**
     * 根据code和dictKey获取值
     * @param code code
     * @param dictKey key
     * @return Result
     */
    @GetMapping(ProviderConstants.PROVIDER_DICT_VALUE)
    R<String> getValue(@RequestParam("code") String code, @RequestParam("dictKey") String dictKey);

    /**
     * 根据code获取字典列表
     * @param code　code
     * @return Result
     */
    @GetMapping(ProviderConstants.PROVIDER_DICT_LIST)
    R<List<SysDict>> getList(@RequestParam("code") String code);

}
