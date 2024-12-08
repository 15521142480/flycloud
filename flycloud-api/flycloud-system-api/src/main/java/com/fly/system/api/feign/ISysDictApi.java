package com.fly.system.api.feign;

import com.fly.common.constant.ServerNames;
import com.fly.common.domain.model.R;
import com.fly.system.api.constants.SystemFeignApiConstants;
import com.fly.system.api.domain.SysDictData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典远程调用接口类
 *
 */
@FeignClient(value = ServerNames.SYSTEM_SERVER_NAME, contextId = "SysDictApi")
public interface ISysDictApi {

    /**
     * 根据code和dictKey获取值
     * @param code code
     * @param dictKey key
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_DICT_VALUE)
    R<String> getValue(@RequestParam("code") String code, @RequestParam("dictKey") String dictKey);

    /**
     * 根据code获取字典列表
     * @param code　code
     * @return Result
     */
    @GetMapping(SystemFeignApiConstants.PROVIDER_DICT_LIST)
    R<List<SysDictData>> getList(@RequestParam("code") String code);

}
